package org.agileinsider.concordion;

import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.event.ScenarioListener;
import org.concordion.api.extension.ConcordionExtender;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.agileinsider.concordion.ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE;
import static org.agileinsider.concordion.ScenarioExtension.SCENARIO_COMMAND;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioExtensionTest {
    @Mock
    private ConcordionExtender concordionExtender;
    @Mock
    private ScenarioListener resultRenderer;
    @Mock
    private ScenarioCommand scenarioCommand;

    private ScenarioExtension ignoreExtension;

    @Before
    public void setUp() throws Exception {
        ignoreExtension = new ScenarioExtension(scenarioCommand, resultRenderer);
    }

    @Test
    public void shouldRegisterCommandCorrectly() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withCommand(CONCORDION_PLUS_NAMESPACE, SCENARIO_COMMAND, scenarioCommand);
    }

    @Test
    public void shouldRegisterRendererWithCommand() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(scenarioCommand).addScenarioListener(resultRenderer);
    }

    @Test
    public void shouldRegisterCssWhichIncludesSkipStyle() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withEmbeddedCSS(withStyles(".pass", ".fail", ".error"));
    }

    private String withStyles(String... styles) {
        LinkedList<Matcher<? extends String>> matchers = new LinkedList<Matcher<? extends String>>();
        for (String style : styles) {
            matchers.add(new Contains(style));
        }
        return argThat(allOf(matchers));
    }
}
