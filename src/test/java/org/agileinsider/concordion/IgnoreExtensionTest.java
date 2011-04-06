package org.agileinsider.concordion;

import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.event.IgnoreListener;
import org.concordion.api.extension.ConcordionExtender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.agileinsider.concordion.ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE;
import static org.agileinsider.concordion.IgnoreExtension.IGNORE_COMMAND;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IgnoreExtensionTest {
    @Mock
    private ConcordionExtender concordionExtender;
    @Mock
    private IgnoreListener resultRenderer;
    @Mock
    private IgnoreCommand ignoreCommand;

    private IgnoreExtension ignoreExtension;

    @Before
    public void setUp() throws Exception {
        ignoreExtension = new IgnoreExtension(ignoreCommand, resultRenderer);
    }

    @Test
    public void shouldRegisterCommandCorrectly() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withCommand(CONCORDION_PLUS_NAMESPACE, IGNORE_COMMAND, ignoreCommand);
    }

    @Test
    public void shouldRegisterRendererWithCommand() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(ignoreCommand).addIgnoreListener(resultRenderer);
    }

    @Test
    public void shouldRegisterCssWhichIncludesSkipStyle() throws Exception {
        ignoreExtension.addTo(concordionExtender);

        verify(concordionExtender).withEmbeddedCSS(contains(".skip"));
    }
}
