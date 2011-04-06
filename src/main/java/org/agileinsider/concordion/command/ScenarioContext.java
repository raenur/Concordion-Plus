package org.agileinsider.concordion.command;

import ognl.OgnlContext;
import org.concordion.api.Evaluator;
import org.concordion.internal.OgnlEvaluator;

import java.lang.reflect.Field;

public class ScenarioContext {
    private static final String ROOT_OBJECT_FIELD_NAME = "rootObject";
    private static final String OGNL_CONTEXT_FIELD_NAME = "ognlContext";

    private static final Field ROOT_OBJECT_FIELD;
    private static final Field OGNL_CONTEXT_FIELD;

    private ScenarioContext parentContext = null;
    private Object rootObject = null;
    private OgnlContext ognlContext = null;
    private ScenarioResultRecorder resultRecorder = null;

    static {
        try {
            ROOT_OBJECT_FIELD = OgnlEvaluator.class.getDeclaredField(ROOT_OBJECT_FIELD_NAME);
            OGNL_CONTEXT_FIELD = OgnlEvaluator.class.getDeclaredField(OGNL_CONTEXT_FIELD_NAME);
            ROOT_OBJECT_FIELD.setAccessible(true);
            OGNL_CONTEXT_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Don't seem to be able to access the fields in concordion using reflection!!!");
        }
    }

    public ScenarioContext() {
    }

    private ScenarioContext(ScenarioContext parentContext, Object rootObject, OgnlContext ognlContext, ScenarioResultRecorder resultRecorder) {
        this.parentContext = parentContext;
        this.rootObject = rootObject;
        this.ognlContext = ognlContext;
        this.resultRecorder = resultRecorder;
    }

    public ScenarioContext create(Evaluator evaluator) {
        try {
            this.rootObject = ROOT_OBJECT_FIELD.get(evaluator);
            this.ognlContext = (OgnlContext) OGNL_CONTEXT_FIELD.get(evaluator);

            Object newRootObject = rootObject.getClass().newInstance();
            OgnlContext newOgnlContext = new OgnlContext();
            ScenarioResultRecorder newResultRecorder = new ScenarioResultRecorder();

            ROOT_OBJECT_FIELD.set(evaluator, newRootObject);
            OGNL_CONTEXT_FIELD.set(evaluator, newOgnlContext);

            return new ScenarioContext(this, newRootObject, newOgnlContext, newResultRecorder);
        } catch (Exception e) {
            throw new IllegalStateException("Don't seem to be able to access the fields in concordion using reflection!!!");
        }
    }

    public ScenarioContext getParent(Evaluator evaluator) {
        try {
            ROOT_OBJECT_FIELD.set(evaluator, parentContext.rootObject);
            OGNL_CONTEXT_FIELD.set(evaluator, parentContext.ognlContext);

            return parentContext;
        } catch (Exception e) {
            throw new IllegalStateException("Don't seem to be able to access the fields in concordion using reflection!!!");
        }
    }

    public Object getRootObject() {
        return rootObject;
    }

    public ScenarioResultRecorder getResultRecorder() {
        return resultRecorder;
    }
}
