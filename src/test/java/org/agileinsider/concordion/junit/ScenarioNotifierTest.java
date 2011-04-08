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

import org.agileinsider.concordion.event.*;

import org.concordion.api.listener.ThrowableCaughtEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioNotifierTest {
    private static final String SCENARIO_NAME = "Scenario Name";

    @Mock
    private NotifierFactory notifierFactory;
    @Mock
    private EachTestNotifier testNotifier;

    private ScenarioNotifier scenarioNotifier;

    private ScenarioStartEvent startEvent = new ScenarioStartEvent(SCENARIO_NAME);

    @Before
    public void setUp() throws Exception {
        when(notifierFactory.createNotifier(startEvent)).thenReturn(testNotifier);

        scenarioNotifier = new ScenarioNotifier(notifierFactory);
        scenarioNotifier.scenarioStarted(startEvent);

        Mockito.reset(testNotifier);
    }

    @Test
    public void shouldNotifyJunitOnSuccess() throws Exception {
        scenarioNotifier.successReported(new ScenarioSuccessEvent(SCENARIO_NAME, null));

        verify(testNotifier).fireTestFinished();
        verifyNoMoreInteractions(testNotifier);
    }

    @Test
    public void shouldPassExceptionToJunitWhenAnErrorOccurs() throws Exception {
        Throwable anException = new IllegalStateException();

        scenarioNotifier.scenarioError(new ScenarioErrorEvent(SCENARIO_NAME, null, anException));

        verify(testNotifier).addFailure(anException);
        verifyNoMoreInteractions(testNotifier);
    }

    @Test
    public void shouldThrowAnAssumptionViolatedExceptionForFailures() throws Exception {
        scenarioNotifier.failureReported(new ScenarioFailureEvent(SCENARIO_NAME, null));

        verify(testNotifier).addFailedAssumption(any(AssumptionViolatedException.class));
        verifyNoMoreInteractions(testNotifier);

    }

    @Test
    public void shouldOnlyCreateNotifierAndFireStartEventWhenScenarioStarts() throws Exception {
        scenarioNotifier.scenarioStarted(startEvent);

        verify(testNotifier).fireTestStarted();
        verifyNoMoreInteractions(testNotifier);
    }

    @Test
    public void shouldNotDoAnythingWhenScenarioFinishes() throws Exception {
        scenarioNotifier.scenarioFinished(new ScenarioFinishEvent(SCENARIO_NAME));

        verifyNoMoreInteractions(testNotifier);
    }

    @Test
    public void shouldPassThrowableToJunitWhenCaught() throws Exception {
        Throwable anException = new IllegalStateException();

        scenarioNotifier.throwableCaught(new ThrowableCaughtEvent(anException, null, null));

        verify(testNotifier).addFailure(anException);
        verifyNoMoreInteractions(testNotifier);

    }
}
