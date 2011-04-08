package org.agileinsider.concordion.junit;

/*
 * Copyright 2011 Mark Barnes (mark@agileinsider.org)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

public class ConcordionPlus extends BlockJUnit4ClassRunner {
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

    @Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> children = new ArrayList<FrameworkMethod>();
        children.addAll(super.getChildren());
        children.add(specificationMethod);
        return children;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        if (method == specificationMethod) {
            return statementBuilder.withFixture(test).buildStatement();
        }
        return super.methodInvoker(method, test);
    }

    @SuppressWarnings({"deprecation"})
    @Override
    protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);
    }

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
}
