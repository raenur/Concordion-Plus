package org.agileinsider.concordion.event;

import org.concordion.api.Element;

public class ScenarioSuccessEvent extends ScenarioProcessingEvent {
    public ScenarioSuccessEvent(String name, Element element) {
        super(name, element);
    }

}
