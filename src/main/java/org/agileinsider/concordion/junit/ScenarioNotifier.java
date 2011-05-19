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

import junit.framework.AssertionFailedError;
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
