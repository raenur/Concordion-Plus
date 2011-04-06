package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.event.IgnoreEvent;
import org.agileinsider.concordion.event.IgnoreListener;
import org.junit.internal.runners.model.EachTestNotifier;

public class IgnoreNotifier implements IgnoreListener {
    private final NotifierFactory notifierFactory;

    public IgnoreNotifier(NotifierFactory notifierFactory) {
        this.notifierFactory = notifierFactory;
    }

    public void ignoredReported(IgnoreEvent event) {
        EachTestNotifier testNotifier = notifierFactory.createScenarioNotifier(event);
        testNotifier.fireTestIgnored();
    }
}
