package org.agileinsider.concordion;

import org.agileinsider.concordion.testutil.ConcordionTestHarness;
import org.agileinsider.concordion.testutil.ProcessingResult;
import org.junit.After;
import org.junit.Before;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcordionPlusAcceptanceTest {
    private ConcordionTestHarness concordionTestHarness;
    private ProcessingResult processingResult;

    @Before
    public void installExtensions() {
        System.setProperty("concordion.extensions", ConcordionPlusExtension.class.getName());
    }

    public String getOutput() {
        return processingResult.getOutputFragmentXML();
    }

    public String outputMatches(String expected) {
        String actual = getOutput();
        Pattern pattern = Pattern.compile(expected, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(actual);
        return matcher.matches() ? expected : actual;
    }

    public String outputContains(String expected) {
        String actual = getOutput();
        return actual.contains(expected) ? expected : actual;
    }

    public void process(String fragment) throws Exception {

        concordionTestHarness = new ConcordionTestHarness(this);
        processingResult = concordionTestHarness.processFragment(fragment);
    }

    public String hasStyle(String style) {
        String styles = processingResult.getStyles();
        if (styles.contains(style)) {
            return style;
        }
        return "not found: " + styles;
    }


    @After
    public void teardown() {
        System.clearProperty("concordion.extensions");
    }
}
