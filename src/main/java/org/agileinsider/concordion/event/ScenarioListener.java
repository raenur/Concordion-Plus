package org.agileinsider.concordion.event;

import org.concordion.api.listener.ThrowableCaughtListener;

public interface ScenarioListener extends ThrowableCaughtListener {
    void ignoredReported(ScenarioIgnoredEvent scenarioIgnoredEvent);

    void successReported(ScenarioSuccessEvent scenarioSuccessEvent);

    void failureReported(ScenarioFailureEvent scenarioFailureEvent);

    void scenarioStarted(ScenarioStartEvent scenarioStartEvent);

    void scenarioFinished(ScenarioFinishEvent scenarioFinishEvent);

    void scenarioError(ScenarioErrorEvent scenarioErrorEvent);
}
