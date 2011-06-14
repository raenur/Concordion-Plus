package org.agileinsider.concordion.render;

import org.agileinsider.concordion.event.ScenarioIgnoredEvent;
import org.concordion.api.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ScenarioResultRendererTest {

    private ScenarioResultRenderer resultRenderer;

    @Before
    public void setUp() throws Exception {
        resultRenderer = new ScenarioResultRenderer();
    }

    @Test
    public void shouldAddSkipCssClassToElement() throws Exception {
        Element element = new Element("Element");
        ScenarioIgnoredEvent event = new ScenarioIgnoredEvent("not used", element);

        resultRenderer.ignoredReported(event);

        String actual = element.toXML();
        assertThat(actual, containsString("class=\"skip\""));
    }
}
