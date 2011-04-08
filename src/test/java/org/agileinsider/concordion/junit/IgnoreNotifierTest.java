package org.agileinsider.concordion.junit;

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

import org.agileinsider.concordion.event.IgnoreEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IgnoreNotifierTest {
    private static final String IGNORED_SECTION = "An Ignored Section";

    @Mock
    private NotifierFactory notifierFactory;
    @Mock
    private EachTestNotifier testNotifier;
    private IgnoreEvent anEvent;

    private IgnoreNotifier ignoreNotifier;

    @Before
    public void setUp() throws Exception {
        anEvent = new IgnoreEvent(IGNORED_SECTION, null);
        when(notifierFactory.createNotifier(anEvent)).thenReturn(testNotifier);

        ignoreNotifier = new IgnoreNotifier(notifierFactory);
    }

    @Test
    public void shouldNotifyJunitOnSuccess() throws Exception {
        ignoreNotifier.ignoredReported(anEvent);

        verify(testNotifier).fireTestIgnored();
        verifyNoMoreInteractions(testNotifier);
    }
}
