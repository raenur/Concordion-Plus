package org.agileinsider.concordion;

import org.concordion.api.extension.ConcordionExtender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConcordionPlusExtensionTest {
    @Mock
    private IgnoreExtension ignoreExtension;
    @Mock
    private ScenarioExtension scenarioExtension;
    @Mock
    private ConcordionExtender concordionExtender;

    private ConcordionPlusExtension concordionPlusExtension;

    @Before
    public void setUp() throws Exception {
        concordionPlusExtension = new ConcordionPlusExtension(ignoreExtension, scenarioExtension);
    }

    @Test
    public void shouldRegisterIgnoreExtension() throws Exception {
        concordionPlusExtension.addTo(concordionExtender);

        verify(ignoreExtension).addTo(concordionExtender);
    }

    @Test
    public void shouldRegisterScenarioExtension() throws Exception {
        concordionPlusExtension.addTo(concordionExtender);

        verify(scenarioExtension).addTo(concordionExtender);
    }
}
