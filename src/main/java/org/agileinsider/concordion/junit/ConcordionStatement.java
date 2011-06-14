package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.ConcordionPlusExtension;
import org.agileinsider.concordion.ScenarioExtension;
import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.render.ScenarioResultRenderer;
import org.concordion.Concordion;
import org.concordion.api.FullOGNL;
import org.concordion.api.ResultSummary;
import org.concordion.api.Source;
import org.concordion.api.Target;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.OgnlEvaluatorFactory;
import org.concordion.internal.util.IOUtil;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

public class ConcordionStatement extends Statement {
    private static Source fixedSource;
    private static Target fixedTarget;

    private Class fixtureClass;
    private final Object fixture;
    private RunNotifier notifier;
    private ScenarioCommand scenarioCommand;

    public ConcordionStatement(Class fixtureClass, Object fixture, RunNotifier notifier) {
        this.fixtureClass = fixtureClass;
        this.fixture = fixture;
        this.notifier = notifier;

        scenarioCommand = new ScenarioCommand();
        addScenarioListeners();
    }

    public void evaluate() throws Throwable {
        ConcordionBuilder concordionBuilder = new ConcordionBuilder();
        if (fixture.getClass().isAnnotationPresent(FullOGNL.class)) {
            concordionBuilder.withEvaluatorFactory(new OgnlEvaluatorFactory());
        }
        if (fixedSource != null) {
            concordionBuilder.withSource(fixedSource);
        }
        if (fixedTarget != null) {
            concordionBuilder.withTarget(fixedTarget);
        }
        concordionBuilder.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE,
                ScenarioExtension.SCENARIO_COMMAND,
                scenarioCommand);
        String css = IOUtil.readResourceAsString(ConcordionPlusExtension.CONCORDION_PLUS_CSS);
        concordionBuilder.withEmbeddedCSS(css);

        Concordion concordion = concordionBuilder.build();
        ResultSummary resultSummary = concordion.process(fixture);
        resultSummary.print(System.out, fixture);
        resultSummary.assertIsSatisfied(fixture);
    }

    private void addScenarioListeners() {
        scenarioCommand.addScenarioListener(new ScenarioResultRenderer());
        scenarioCommand.addScenarioListener(new ScenarioNotifier(new NotifierFactory(notifier, fixtureClass)));
    }

    public static void setSource(Source fixedSource) {
        ConcordionStatement.fixedSource = fixedSource;
    }

    public static void setTarget(Target fixedTarget) {
        ConcordionStatement.fixedTarget = fixedTarget;
    }
}
