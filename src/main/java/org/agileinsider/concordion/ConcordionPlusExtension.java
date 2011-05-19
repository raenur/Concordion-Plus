package org.agileinsider.concordion;

/*
 * Copyright 2011 Mark Barnes (mark@agileinsider.org)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
