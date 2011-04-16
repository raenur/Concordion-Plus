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

import org.agileinsider.concordion.junit.ConcordionStatement;

import org.concordion.api.Resource;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class JUnitTestHarness extends ConcordionTestHarness {
    private JUnitTestHarnessRunNotifier notifier;

    public JUnitTestHarness(Object fixture) {
        super(fixture);
        notifier = new JUnitTestHarnessRunNotifier();
    }

    @Override
    protected void process(Object fixture, Resource resource, StubSource stubSource, StubTarget stubTarget) throws Throwable {
        ConcordionStatement.setSource(stubSource);
        ConcordionStatement.setTarget(stubTarget);
        RunnerBuilder builder = new AllDefaultPossibilitiesBuilder(true);
        Runner runner = builder.runnerForClass(fixture.getClass());
        runner.run(notifier);
    }

    public JUnitTestHarnessRunNotifier getNotifier() {
        return notifier;
    }
}
