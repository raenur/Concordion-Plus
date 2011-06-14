package org.agileinsider.concordion.render;

import org.agileinsider.concordion.event.*;
import org.concordion.api.Element;
import org.concordion.internal.listener.ThrowableRenderer;

public class ScenarioResultRenderer extends ThrowableRenderer implements ScenarioListener {
    public void successReported(ScenarioSuccessEvent event) {
        addStyle(event, "pass");
    }

    public void failureReported(ScenarioFailureEvent event) {
        addStyle(event, "fail");
    }

    public void scenarioError(ScenarioErrorEvent event) {
        addStyle(event, "error");
    }

    public void ignoredReported(ScenarioIgnoredEvent event) {
        Element element = event.getElement();
        element.addStyleClass("skip").appendNonBreakingSpaceIfBlank();
    }


    public void scenarioStarted(ScenarioStartEvent event) {
    }

    public void scenarioFinished(ScenarioFinishEvent event) {
    }

    private void addStyle(ScenarioProcessingEvent event, String style) {
        Element element = event.getElement();
        element.addStyleClass(style).appendNonBreakingSpaceIfBlank();
    }
}
