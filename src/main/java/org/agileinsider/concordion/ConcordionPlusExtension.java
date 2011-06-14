package org.agileinsider.concordion;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class ConcordionPlusExtension implements ConcordionExtension {
    public static final String CONCORDION_PLUS_NAMESPACE = "http://www.agileinsider.org/concordion/plus";
    public static final String CONCORDION_PLUS_CSS = "/org/agileinsider/concordion/concordion-plus.css";

    private final ScenarioExtension scenarioExtension;

    public ConcordionPlusExtension() {
        this(new ScenarioExtension());
    }

    public ConcordionPlusExtension(ScenarioExtension scenarioExtension) {
        this.scenarioExtension = scenarioExtension;
    }

    public void addTo(ConcordionExtender concordionExtender) {
        scenarioExtension.addTo(concordionExtender);
    }
}
