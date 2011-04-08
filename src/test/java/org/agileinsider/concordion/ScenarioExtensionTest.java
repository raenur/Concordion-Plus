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

import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.event.ScenarioListener;

import org.concordion.api.extension.ConcordionExtender;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.agileinsider.concordion.ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE;
import static org.agileinsider.concordion.ScenarioExtension.SCENARIO_COMMAND;

import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioExtensionTest {
    @Mock
    private ConcordionExtender concordionExtender;
    @Mock
    private ScenarioListener resultRenderer;
    @Mock
    private ScenarioCommand scenarioCommand;

    private ScenarioExtension ignoreExtension;

    @Before
    public void setUp() throws Exception {
        ignoreExtension = new ScenarioExtension(scenarioCommand, resultRenderer);
    }

    @Test
    public void shouldRegisterCommandCorrectly() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withCommand(CONCORDION_PLUS_NAMESPACE, SCENARIO_COMMAND, scenarioCommand);
    }

    @Test
    public void shouldRegisterRendererWithCommand() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(scenarioCommand).addScenarioListener(resultRenderer);
    }

    @Test
    public void shouldRegisterCssWhichIncludesSkipStyle() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withEmbeddedCSS(withStyles(".pass", ".fail", ".error"));
    }

    private String withStyles(String... styles) {
        LinkedList<Matcher<? extends String>> matchers = new LinkedList<Matcher<? extends String>>();
        for (String style : styles) {
            matchers.add(new Contains(style));
        }
        return argThat(allOf(matchers));
    }
}
