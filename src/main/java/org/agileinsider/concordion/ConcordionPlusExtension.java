package org.agileinsider.concordion;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class ConcordionPlusExtension implements ConcordionExtension {
    public static final String CONCORDION_PLUS_NAMESPACE = "http://www.agileinsider.org/concordion/plus";
    private final IgnoreExtension ignoreExtension;
    private final ScenarioExtension scenarioExtension;

    public ConcordionPlusExtension() {
        this(new IgnoreExtension(), new ScenarioExtension());
    }

    public ConcordionPlusExtension(IgnoreExtension ignoreExtension, ScenarioExtension scenarioExtension) {
        this.ignoreExtension = ignoreExtension;
        this.scenarioExtension = scenarioExtension;
    }

    public void addTo(ConcordionExtender concordionExtender) {
        ignoreExtension.addTo(concordionExtender);
        scenarioExtension.addTo(concordionExtender);
    }
}
