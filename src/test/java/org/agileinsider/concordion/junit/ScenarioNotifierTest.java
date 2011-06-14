package org.agileinsider.concordion.junit;

import junit.framework.AssertionFailedError;
import org.agileinsider.concordion.event.*;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.junit.Before;
import org.junit.Test;
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
    public void shouldAddAnAssertionFailedErrorForFailures() throws Exception {
        scenarioNotifier.failureReported(new ScenarioFailureEvent(SCENARIO_NAME, null));

        verify(testNotifier).addFailure(any(AssertionFailedError.class));
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
