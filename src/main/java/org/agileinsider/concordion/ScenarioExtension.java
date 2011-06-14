package org.agileinsider.concordion;

import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.event.ScenarioListener;
import org.agileinsider.concordion.render.ScenarioResultRenderer;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.util.IOUtil;

public class ScenarioExtension implements ConcordionExtension {
    public static final String SCENARIO_COMMAND = "scenario";
    public static final String IGNORE_COMMAND = "ignore";

    private final ScenarioCommand scenarioCommand;
    private final ScenarioListener resultRenderer;

    public ScenarioExtension() {
        this(new ScenarioCommand(), new ScenarioResultRenderer());
    }

    protected ScenarioExtension(ScenarioCommand scenarioCommand, ScenarioListener resultRenderer) {
        this.scenarioCommand = scenarioCommand;
        this.resultRenderer = resultRenderer;
    }


    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE, SCENARIO_COMMAND, scenarioCommand);
        scenarioCommand.addScenarioListener(resultRenderer);
        String stylesheetContent = IOUtil.readResourceAsString(ConcordionPlusExtension.CONCORDION_PLUS_CSS);
        concordionExtender.withEmbeddedCSS(stylesheetContent);
    }
}
