package org.agileinsider.concordion.junit;

import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;

public class SpecificationFrameworkMethod extends FrameworkMethod {
    private final String specificationDescription;

    public SpecificationFrameworkMethod(String specificationDescription) {
        super(null);
        this.specificationDescription = specificationDescription;
    }

    public String getName() {
        return specificationDescription;
    }

    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return null;
    }
}
