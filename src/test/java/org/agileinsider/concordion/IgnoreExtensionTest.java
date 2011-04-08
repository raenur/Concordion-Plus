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

import org.concordion.api.extension.ConcordionExtender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.agileinsider.concordion.ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE;
import static org.agileinsider.concordion.IgnoreExtension.IGNORE_COMMAND;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IgnoreExtensionTest {
    @Mock
    private ConcordionExtender concordionExtender;
    @Mock
    private IgnoreListener resultRenderer;
    @Mock
    private IgnoreCommand ignoreCommand;

    private IgnoreExtension ignoreExtension;

    @Before
    public void setUp() throws Exception {
        ignoreExtension = new IgnoreExtension(ignoreCommand, resultRenderer);
    }

    @Test
    public void shouldRegisterCommandCorrectly() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withCommand(CONCORDION_PLUS_NAMESPACE, IGNORE_COMMAND, ignoreCommand);
    }

    @Test
    public void shouldRegisterRendererWithCommand() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(ignoreCommand).addIgnoreListener(resultRenderer);
    }

    @Test
    public void shouldRegisterCssWhichIncludesSkipStyle() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withEmbeddedCSS(contains(".skip"));
    }
}
