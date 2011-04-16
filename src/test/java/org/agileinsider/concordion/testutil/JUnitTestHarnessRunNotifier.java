package org.agileinsider.concordion.testutil;

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
