package org.agileinsider.concordion.junit;

import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Concordion Plus runner that just executes the specificationMethod, ignoring any standard
 * JUnit annotated @Before, @After or @Test methods. Setup and Teardown fixtures are
 * handled by the ScenarioCommand.
 */
public class ConcordionPlus extends ParentRunner<FrameworkMethod> {


    private final Description specificationDescription;
    private final FrameworkMethod specificationMethod;
    private final ConcordionStatementBuilder statementBuilder;

    public ConcordionPlus(Class<?> fixtureClass) throws InitializationError {
        super(fixtureClass);
        String testDescription = "Executable Specification: '" + fixtureClass.getSimpleName().replaceAll("Test$", "") + "'";
        specificationDescription = Description.createTestDescription(fixtureClass, testDescription);
        statementBuilder = new ConcordionStatementBuilder(fixtureClass);
        try {
            specificationMethod = new SpecificationFrameworkMethod(testDescription);
        } catch (Exception e) {
            throw new InitializationError("Failed to initialize ConcordionPlus");
        }
    }

	//
	// Implementation of ParentRunner
	//

	@Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        statementBuilder.withRunNotifier(notifier);

        EachTestNotifier specificationNotifier = new EachTestNotifier(notifier, specificationDescription);
        specificationNotifier.fireTestStarted();
        try {
            methodBlock(method).evaluate();
        } catch (AssumptionViolatedException e) {
            specificationNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            specificationNotifier.addFailure(e);
        } finally {
            specificationNotifier.fireTestFinished();
        }
    }

	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(getTestClass().getJavaClass(),
				testName(method), method.getAnnotations());
	}

@Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> children = new ArrayList<FrameworkMethod>();
        children.add(specificationMethod);
        return children;
    }

	@Override
	protected void collectInitializationErrors(List<Throwable> errors) {
		super.collectInitializationErrors(errors);

		validateConstructor(errors);
		validateFields(errors);
	}

	/**
	 * Adds to {@code errors} if the test class has more than one constructor,
	 * or if the constructor takes parameters. Override if a subclass requires
	 * different validation rules.
	 */
	protected void validateConstructor(List<Throwable> errors) {
		validateOnlyOneConstructor(errors);
		validateZeroArgConstructor(errors);
	}

	/**
	 * Adds to {@code errors} if the test class has more than one constructor
	 * (do not override)
	 */
	protected void validateOnlyOneConstructor(List<Throwable> errors) {
		if (!hasOneConstructor()) {
			String gripe= "Test class should have exactly one public constructor";
			errors.add(new Exception(gripe));
		}
	}

	/**
	 * Adds to {@code errors} if the test class's single constructor takes
	 * parameters (do not override)
	 */
	private void validateZeroArgConstructor(List<Throwable> errors) {
		if (hasOneConstructor()
				&& !(getTestClass().getOnlyConstructor().getParameterTypes().length == 0)) {
			String gripe= "Test class should have exactly one public zero-argument constructor";
			errors.add(new Exception(gripe));
		}
	}

	private boolean hasOneConstructor() {
		return getTestClass().getJavaClass().getConstructors().length == 1;
	}

	private void validateFields(List<Throwable> errors) {
		for (FrameworkField each : getTestClass()
				.getAnnotatedFields(Rule.class))
			validateRuleField(each.getField(), errors);
	}

	private void validateRuleField(Field field, List<Throwable> errors) {
		if (!MethodRule.class.isAssignableFrom(field.getType()))
			errors.add(new Exception("Field " + field.getName()
					+ " must implement MethodRule"));
		if (!Modifier.isPublic(field.getModifiers()))
			errors.add(new Exception("Field " + field.getName()
					+ " must be public"));
	}

	/**
	 * Returns a new fixture for running a test. Default implementation executes
	 * the test class's no-argument constructor (validation should have ensured
	 * one exists).
	 */
	protected Object createTest() throws Exception {
		return getTestClass().getOnlyConstructor().newInstance();
	}

	/**
	 * Returns the name that describes {@code method} for {@link org.junit.runner.Description}s.
	 * Default implementation is the method's name
	 */
	protected String testName(FrameworkMethod method) {
		return method.getName();
	}

	/**
	 * Returns a Statement that, when executed, either returns normally if
	 * {@code method} passes, or throws an exception if {@code method} fails.
	 *  */

	protected Statement methodBlock(FrameworkMethod method) {
		Object test;
		try {
			test= new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return createTest();
				}
			}.run();
		} catch (Throwable e) {
			return new Fail(e);
		}

		Statement statement= methodInvoker(method, test);
		statement= withRules(method, test, statement);
		return statement;
	}

	//
	// Statement builders
	//


    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        if (method == specificationMethod) {
            return statementBuilder.withFixture(test).buildStatement();
        }
        return new InvokeMethod(method, test);
    }


	private Statement withRules(FrameworkMethod method, Object target,
			Statement statement) {
		Statement result= statement;
		for (MethodRule each : getTestClass().getAnnotatedFieldValues(target,
				Rule.class, MethodRule.class))
			result= each.apply(result, method, target);
		return result;
	}

}
