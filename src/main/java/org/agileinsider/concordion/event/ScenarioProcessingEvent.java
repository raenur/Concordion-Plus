package org.agileinsider.concordion.event;

import org.concordion.api.Element;

public class ScenarioProcessingEvent extends ScenarioEvent {
    protected final Element element;

    protected ScenarioProcessingEvent(String scenarioName, Element element) {
        super(scenarioName);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
