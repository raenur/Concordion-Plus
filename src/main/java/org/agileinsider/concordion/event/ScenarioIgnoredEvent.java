package org.agileinsider.concordion.event;

import org.concordion.api.Element;

public class ScenarioIgnoredEvent extends ScenarioEvent {
    private final Element element;

    public ScenarioIgnoredEvent(String name, Element element) {
        super(name);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

}
