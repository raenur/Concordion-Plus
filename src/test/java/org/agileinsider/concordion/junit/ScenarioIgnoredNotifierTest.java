package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.event.ScenarioIgnoredEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioIgnoredNotifierTest {
    private static final String IGNORED_SECTION = "An Ignored Section";

    @Mock
    private NotifierFactory notifierFactory;
    @Mock
    private EachTestNotifier testNotifier;
    private ScenarioIgnoredEvent anEvent;

    private ScenarioNotifier ignoreNotifier;

    @Before
    public void setUp() throws Exception {
        anEvent = new ScenarioIgnoredEvent(IGNORED_SECTION, null);
        when(notifierFactory.createNotifier(anEvent)).thenReturn(testNotifier);

        ignoreNotifier = new ScenarioNotifier(notifierFactory);
    }

    @Test
    public void shouldNotifyJunitOnSuccess() throws Exception {
        ignoreNotifier.ignoredReported(anEvent);

        verify(testNotifier).fireTestIgnored();
        verifyNoMoreInteractions(testNotifier);
    }
}
