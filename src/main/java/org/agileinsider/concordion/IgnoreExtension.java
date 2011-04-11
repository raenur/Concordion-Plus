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

import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.event.IgnoreListener;
import org.agileinsider.concordion.render.IgnoreResultRenderer;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.util.IOUtil;

public class IgnoreExtension implements ConcordionExtension {
    public static final String IGNORE_COMMAND = "ignore";

    private final IgnoreCommand ignoreCommand;
    private final IgnoreListener resultRenderer;

    public IgnoreExtension() {
        this(new IgnoreCommand(), new IgnoreResultRenderer());
    }

    protected IgnoreExtension(IgnoreCommand ignoreCommand, IgnoreListener resultRenderer) {
        this.ignoreCommand = ignoreCommand;
        this.resultRenderer = resultRenderer;
    }

    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE, IGNORE_COMMAND, ignoreCommand);
        ignoreCommand.addIgnoreListener(resultRenderer);
        String stylesheetContent = IOUtil.readResourceAsString(ConcordionPlusExtension.CONCORDION_PLUS_CSS);
        concordionExtender.withEmbeddedCSS(stylesheetContent);
    }
}
