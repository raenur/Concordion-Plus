package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.ConcordionPlusExtension;
import org.agileinsider.concordion.IgnoreExtension;
import org.agileinsider.concordion.ScenarioExtension;
import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.render.IgnoreResultRenderer;
import org.agileinsider.concordion.render.ScenarioResultRenderer;
import org.concordion.api.FullOGNL;
import org.concordion.api.ResultSummary;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.OgnlEvaluatorFactory;
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

    private void addScenarioListeners() {
        scenarioCommand.addScenarioListener(new ScenarioResultRenderer());
        scenarioCommand.addScenarioListener(new ScenarioNotifier(new NotifierFactory(notifier, fixtureClass)));
    }

    private void addIgnoreListeners() {
        ignoreCommand.addIgnoreListener(new IgnoreResultRenderer());
        ignoreCommand.addIgnoreListener(new IgnoreNotifier(new NotifierFactory(notifier, fixtureClass)));
    }

}
