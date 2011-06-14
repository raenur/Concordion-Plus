package org.agileinsider.concordion.testutil;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import java.util.HashSet;
import java.util.Set;

public class JUnitTestHarnessRunNotifier extends RunNotifier {
    Set<String> failures = new HashSet<String>();
    Set<String> ignored = new HashSet<String>();
    Set<String> started = new HashSet<String>();
    Set<String> finished = new HashSet<String>();

    @Override
    public void fireTestStarted(Description description) throws StoppedByUserException {
        super.fireTestStarted(description);
        started.add(description.getDisplayName());
    }

    @Override
    public void fireTestFailure(Failure failure) {
        super.fireTestFailure(failure);
        failures.add(failure.getDescription().getDisplayName());
    }

    @Override
    public void fireTestAssumptionFailed(Failure failure) {
        super.fireTestAssumptionFailed(failure);
        failures.add(failure.getDescription().getDisplayName());
    }

    @Override
    public void fireTestIgnored(Description description) {
        super.fireTestIgnored(description);
        ignored.add(description.getDisplayName());
    }

    @Override
    public void fireTestFinished(Description description) {
        super.fireTestFinished(description);
        finished.add(description.getDisplayName());
    }

    public boolean wasSuccessful() {
        return started.size() == finished.size() && failures.isEmpty();
    }
}
