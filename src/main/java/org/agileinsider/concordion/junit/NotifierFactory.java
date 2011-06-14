package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.event.ScenarioEvent;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class NotifierFactory {
    private final RunNotifier notifier;
    private final Class<?> fixtureClass;

    public NotifierFactory(RunNotifier notifier, Class<?> fixtureClass) {
        this.notifier = notifier;
        this.fixtureClass = fixtureClass;
    }

    public EachTestNotifier createNotifier(ScenarioEvent event) {
        Description testDescription = Description.createTestDescription(fixtureClass, "- Scenario: " + event.getScenarioName());
        return new EachTestNotifier(notifier, testDescription);
    }
}
