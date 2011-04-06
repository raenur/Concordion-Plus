package org.agileinsider.concordion.event;

public class ScenarioEvent {
    private final String scenarioName;

    protected ScenarioEvent(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getScenarioName() {
        return scenarioName;
    }
}
