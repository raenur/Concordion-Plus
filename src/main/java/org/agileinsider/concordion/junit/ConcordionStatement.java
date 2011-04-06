package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.ConcordionPlusExtension;
import org.agileinsider.concordion.IgnoreExtension;
import org.agileinsider.concordion.ScenarioExtension;
import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.event.*;
import org.agileinsider.concordion.render.IgnoreResultRenderer;
import org.agileinsider.concordion.render.ScenarioResultRenderer;
import org.concordion.api.FullOGNL;
import org.concordion.api.ResultSummary;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.OgnlEvaluatorFactory;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

class ConcordionStatement extends Statement {
    private Class fixtureClass;
    private final Object fixture;
    private RunNotifier notifier;
    private IgnoreCommand ignoreCommand;
    private ScenarioCommand scenarioCommand;

    public ConcordionStatement(Class fixtureClass, Object fixture, RunNotifier notifier) {
        this.fixtureClass = fixtureClass;
        this.fixture = fixture;
        this.notifier = notifier;

        ignoreCommand = new IgnoreCommand();
        scenarioCommand = new ScenarioCommand();
        addIgnoreListeners();
        addScenarioListeners();
    }

    public void evaluate() throws Throwable {
        ConcordionBuilder concordionBuilder = new ConcordionBuilder();
        if (fixture.getClass().isAnnotationPresent(FullOGNL.class)) {
            concordionBuilder.withEvaluatorFactory(new OgnlEvaluatorFactory());
        }
        concordionBuilder.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE,
                ScenarioExtension.SCENARIO_COMMAND,
                scenarioCommand);
        concordionBuilder.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE,
                IgnoreExtension.IGNORE_COMMAND,
                ignoreCommand);
        ResultSummary resultSummary = concordionBuilder.build().process(fixture);
        resultSummary.print(System.out, fixture);
        resultSummary.assertIsSatisfied(fixture);
    }

    private EachTestNotifier createScenarioEventNotifier(RunNotifier notifier, ScenarioEvent event) {
        return new EachTestNotifier(notifier, Description.createTestDescription(fixtureClass, "- Scenario: " + event.getScenarioName()));
    }

    private void addScenarioListeners() {
        scenarioCommand.addScenarioListener(new ScenarioResultRenderer());
        scenarioCommand.addScenarioListener(new ScenarioTestNotifier());
    }

    private void addIgnoreListeners() {
        ignoreCommand.addIgnoreListener(new IgnoreResultRenderer());
        ignoreCommand.addIgnoreListener(new IgnoreListener() {
            public void ignoredReported(IgnoreEvent event) {
                createScenarioEventNotifier(notifier, event).fireTestIgnored();
            }
        });
    }

    private class ScenarioTestNotifier implements ScenarioListener {
        EachTestNotifier scenarioEventNotifier;

        public void successReported(ScenarioSuccessEvent event) {
            scenarioEventNotifier.fireTestFinished();
        }

        public void scenarioError(ScenarioErrorEvent event) {
            scenarioEventNotifier.addFailure(event.getThrowable());
        }

        public void failureReported(ScenarioFailureEvent event) {
            scenarioEventNotifier.addFailedAssumption(new AssumptionViolatedException("Scenario failed! Check output for details."));
        }

        public void scenarioStarted(ScenarioStartEvent event) {
            scenarioEventNotifier = createScenarioEventNotifier(notifier, event);
            scenarioEventNotifier.fireTestStarted();
        }

        public void scenarioFinished(ScenarioFinishEvent scenarioFinishEvent) {
            scenarioEventNotifier = null;
        }

        public void throwableCaught(ThrowableCaughtEvent event) {
            scenarioEventNotifier.addFailure(event.getThrowable());
        }
    }
}
