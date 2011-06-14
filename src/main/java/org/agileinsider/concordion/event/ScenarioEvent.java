package org.agileinsider.concordion.event;

public class ScenarioEvent {
    private final String scenarioName;

    public ScenarioEvent(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getScenarioName() {
        return scenarioName;
    }
}
