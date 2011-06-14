package org.agileinsider.concordion.junit;

import org.junit.runner.notification.RunNotifier;

public class ConcordionStatementBuilder {
    private final Class fixtureClass;
    private Object fixture;
    private RunNotifier runNotifier;

    public ConcordionStatementBuilder(Class<?> fixtureClass) {
        this.fixtureClass = fixtureClass;
    }

    public ConcordionStatementBuilder withFixture(Object fixture) {
        this.fixture = fixture;
        return this;
    }

    public ConcordionStatementBuilder withRunNotifier(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
        return this;
    }

    public ConcordionStatement buildStatement() {
        return new ConcordionStatement(fixtureClass, fixture, runNotifier);
    }
}
