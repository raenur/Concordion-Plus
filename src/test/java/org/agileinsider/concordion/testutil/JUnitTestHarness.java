package org.agileinsider.concordion.testutil;

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
