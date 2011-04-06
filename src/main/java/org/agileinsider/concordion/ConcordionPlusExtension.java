package org.agileinsider.concordion;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class ConcordionPlusExtension implements ConcordionExtension {
    public static final String CONCORDION_PLUS_NAMESPACE = "http://www.agileinsider.org/concordion/plus";

    public void addTo(ConcordionExtender concordionExtender) {
        new IgnoreExtension().addTo(concordionExtender);
        new ScenarioExtension().addTo(concordionExtender);
    }
}
