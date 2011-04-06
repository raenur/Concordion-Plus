package org.agileinsider.concordion.command;

import junit.framework.AssertionFailedError;
import org.agileinsider.concordion.event.*;
import org.concordion.api.*;
import org.concordion.internal.util.Announcer;
import org.junit.After;
import org.junit.Before;

public class ScenarioCommand extends AbstractCommand {
    private final Announcer<ScenarioListener> listeners = Announcer.to(ScenarioListener.class);
    private final MethodInvoker methodInvoker;

    private ScenarioContext scenarioContext = new ScenarioContext();

    public ScenarioCommand() {
        this(new SimpleMethodInvoker());
    }

    public ScenarioCommand(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    public void addScenarioListener(ScenarioListener listener) {
        listeners.addListener(listener);
    }

    @Override
    public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        String scenarioName = commandCall.getExpression();
        startScenarioContext(scenarioName, evaluator);
    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        commandCall.getChildren().processSequentially(evaluator, scenarioContext.getResultRecorder());
    }

    @Override
    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        String scenarioName = commandCall.getExpression();
        ScenarioResultRecorder scenarioResults = scenarioContext.getResultRecorder();

        if (scenarioResults.getExceptionCount() > 0) {
            announceError(scenarioName, commandCall.getElement(), new AssertionFailedError("Scenario has errors."));
        } else if (scenarioResults.getFailureCount() > 0) {
            announceFail(scenarioName, commandCall.getElement());
        } else {
            announceSuccess(scenarioName, commandCall.getElement());
        }
        endScenarioContext(scenarioName, evaluator);
    }

    private void announceStart(String scenario) {
        listeners.announce().scenarioStarted(new ScenarioStartEvent(scenario));
    }

    private void announceSuccess(String scenario, Element element) {
        listeners.announce().successReported(new ScenarioSuccessEvent(scenario, element));
    }

    private void announceFail(String scenario, Element element) {
        listeners.announce().failureReported(new ScenarioFailureEvent(scenario, element));
    }

    private void announceError(String scenario, Element element, Throwable throwable) {
        listeners.announce().scenarioError(new ScenarioErrorEvent(scenario, element, throwable));
    }

    private void announceFinish(String scenario) {
        listeners.announce().scenarioFinished(new ScenarioFinishEvent(scenario));
    }

    private void startScenarioContext(String scenarioName, Evaluator evaluator) {
        announceStart(scenarioName);
        scenarioContext = scenarioContext.create(evaluator);
        methodInvoker.invokeAnnotatedMethods(scenarioContext, Before.class);
    }

    private void endScenarioContext(String scenarioName, Evaluator evaluator) {
        methodInvoker.invokeAnnotatedMethods(scenarioContext, After.class);
        scenarioContext = scenarioContext.getParent(evaluator);
        announceFinish(scenarioName);
    }
}
