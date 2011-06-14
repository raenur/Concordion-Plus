package org.agileinsider.concordion.event;

import org.concordion.api.Element;

public class ScenarioFailureEvent extends ScenarioProcessingEvent {
    public ScenarioFailureEvent(String name, Element element) {
        super(name, element);
    }
}
