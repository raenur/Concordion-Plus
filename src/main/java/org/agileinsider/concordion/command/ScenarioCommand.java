package org.agileinsider.concordion.command;

import ognl.DefaultMemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import org.agileinsider.concordion.ConcordionPlusExtension;
import org.agileinsider.concordion.ScenarioExtension;
import org.agileinsider.concordion.event.*;
import org.concordion.api.*;
import org.concordion.internal.OgnlEvaluator;
import org.concordion.internal.util.Announcer;
import org.junit.After;
import org.junit.Before;

public class ScenarioCommand extends AbstractCommand {
    private final Announcer<ScenarioListener> listeners = Announcer.to(ScenarioListener.class);
    private final MethodInvoker methodInvoker;

    public ScenarioCommand() {
        this(new MethodInvoker());
    }

    public ScenarioCommand(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    public void addScenarioListener(ScenarioListener listener) {
        listeners.addListener(listener);
    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        Object scenarioFixture = null;
        Element element = commandCall.getElement();
        String ignoreValue = element.getAttributeValue(ScenarioExtension.IGNORE_COMMAND, ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE);
        if (ignoreValue != null && !ignoreValue.isEmpty())
        {
            resultRecorder.record(Result.IGNORED);
            listeners.announce().ignoredReported(new ScenarioIgnoredEvent(commandCall.getExpression(), commandCall.getElement()));
            return;
        }
        try {
            OgnlContext permissiveContext = new OgnlContext();
            permissiveContext.setMemberAccess(new DefaultMemberAccess(true));
            Object rootObject = Ognl.getValue("rootObject", permissiveContext, evaluator);
            scenarioFixture = rootObject.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Nasty hack exposed!!!", e);
        }

        String scenarioName = commandCall.getExpression();
        listeners.announce().scenarioStarted(new ScenarioStartEvent(scenarioName));

        ScenarioResultRecorder scenarioResultRecorder = new ScenarioResultRecorder();

        methodInvoker.invokeAnnotatedMethods(scenarioFixture, Before.class, scenarioResultRecorder);

        OgnlEvaluator scenarioEvaluator = new OgnlEvaluator(scenarioFixture);

        commandCall.getChildren().processSequentially(scenarioEvaluator, scenarioResultRecorder);
        if (scenarioResultRecorder.getExceptionCount() > 0) {
            listeners.announce().scenarioError(new ScenarioErrorEvent(scenarioName, element, new RuntimeException("Scenario has errors.")));
            resultRecorder.record(Result.EXCEPTION);
        } else if (scenarioResultRecorder.getFailureCount() > 0) {
            listeners.announce().failureReported(new ScenarioFailureEvent(scenarioName, element));
            resultRecorder.record(Result.FAILURE);
        } else {
            listeners.announce().successReported(new ScenarioSuccessEvent(scenarioName, element));
            resultRecorder.record(Result.SUCCESS);
        }

        methodInvoker.invokeAnnotatedMethods(scenarioFixture, After.class, scenarioResultRecorder);
        listeners.announce().scenarioFinished(new ScenarioFinishEvent(scenarioName));
    }
}
