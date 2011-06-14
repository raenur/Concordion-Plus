package org.agileinsider.concordion.junit;

import junit.framework.AssertionFailedError;
import org.agileinsider.concordion.event.*;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.junit.internal.runners.model.EachTestNotifier;

public class ScenarioNotifier implements ScenarioListener {
    private EachTestNotifier scenarioEventNotifier;
    private final NotifierFactory notifierFactory;

    public ScenarioNotifier(NotifierFactory notifierFactory) {
        this.notifierFactory = notifierFactory;
    }

    public void successReported(ScenarioSuccessEvent event) {
        scenarioEventNotifier.fireTestFinished();
    }

    public void scenarioError(ScenarioErrorEvent event) {
        scenarioEventNotifier.addFailure(event.getThrowable());
    }

    public void failureReported(ScenarioFailureEvent event) {
        scenarioEventNotifier.addFailure(new AssertionFailedError("Scenario failed! Check output for details."));
    }

    public void scenarioStarted(ScenarioStartEvent event) {
        scenarioEventNotifier = notifierFactory.createNotifier(event);
        scenarioEventNotifier.fireTestStarted();
    }

    public void scenarioFinished(ScenarioFinishEvent scenarioFinishEvent) {
        scenarioEventNotifier = null;
    }

    public void ignoredReported(ScenarioIgnoredEvent event) {
        EachTestNotifier testNotifier = notifierFactory.createNotifier(event);
        testNotifier.fireTestIgnored();
    }

    public void throwableCaught(ThrowableCaughtEvent event) {
        scenarioEventNotifier.addFailure(event.getThrowable());
    }
}
