package org.agileinsider.concordion.command;

import java.lang.annotation.Annotation;

public interface MethodInvoker {
    void invokeAnnotatedMethods(ScenarioContext scenarioContext, Class<? extends Annotation> annotationClass);
}
