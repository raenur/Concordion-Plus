package org.agileinsider.concordion.junit;

import org.agileinsider.concordion.event.ScenarioEvent;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotifierFactoryTest {
    private static final String SCENARIO_NAME = "An Example Scenario";
    @Mock
    private RunNotifier runNotifier;

    private NotifierFactory notifierFactory;

    @Before
    public void setUp() throws Exception {
       notifierFactory = new NotifierFactory(runNotifier, NotifierFactoryTest.class);
    }

    @Test
    public void shouldCreateNotifierWithScenarioNameInDescription() throws Exception {
        ScenarioEvent scenarioEvent = new ScenarioEvent(SCENARIO_NAME);

        EachTestNotifier scenarioNotifier = notifierFactory.createScenarioNotifier(scenarioEvent);
        scenarioNotifier.fireTestStarted();

        verify(runNotifier).fireTestStarted(argThat(descriptionContains(SCENARIO_NAME)));
    }

    private Matcher<Description> descriptionContains(final String scenarioName) {
        return new BaseMatcher<Description>() {
            public boolean matches(Object o) {
                return Description.class.cast(o).getDisplayName().contains(scenarioName);
            }

            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Description with '");
                description.appendText(scenarioName);
                description.appendText("'");
            }
        };
    }
}
