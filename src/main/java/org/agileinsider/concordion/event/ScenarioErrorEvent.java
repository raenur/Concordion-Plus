package org.agileinsider.concordion.event;

import org.concordion.api.Element;

public class ScenarioErrorEvent extends ScenarioProcessingEvent {
    private Throwable throwable;

    public ScenarioErrorEvent(String name, Element element, Throwable throwable) {
        super(name, element);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
