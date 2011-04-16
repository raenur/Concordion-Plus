package org.agileinsider.concordion;

/*
 * Copyright 2011 Mark Barnes (mark@agileinsider.org)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.agileinsider.concordion.testutil.ConcordionTestHarness;
import org.agileinsider.concordion.testutil.JUnitTestHarness;
import org.agileinsider.concordion.testutil.JUnitTestHarnessRunNotifier;
import org.agileinsider.concordion.testutil.ProcessingResult;

import org.concordion.api.ExpectedToFail;
import org.junit.After;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcordionPlusAcceptanceTest {
    private ProcessingResult processingResult;
    private JUnitTestHarness junitTestHarness;

    public String getOutput() {
        return processingResult.getOutputFragmentXML();
    }

    public void processUsingConcordionPlusAnnotation(String fragment) throws Throwable {
        junitTestHarness = new JUnitTestHarness(getFixture());
        processingResult = junitTestHarness.processFragment(fragment);
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

    public void process(String fragment) throws Throwable {
        System.setProperty("concordion.extensions", ConcordionPlusExtension.class.getName());
        ConcordionTestHarness concordionTestHarness = new ConcordionTestHarness(getFixture());
        processingResult = concordionTestHarness.processFragment(fragment);
    }

    public Object getFixture() {
        return this;
    }

    public String hasStyle(String style) {
        String styles = processingResult.getStyles();
        if (styles.contains(style)) {
            return style;
        }
        return "not found: " + styles;
    }

    public String hasRunner(String runner) {
        Object fixture = getFixture();
        if (fixture == null) {
            return "No test fixture being created.";
        }
        RunWith runWith = fixture.getClass().getAnnotation(RunWith.class);
        if (runWith == null) {
            return "No RunWith Annoation found";
        }
        return runWith.value().getSimpleName();
    }

    public String ensureExpectedToFail(String successValue) {
        Object fixture = getFixture();
        if (fixture == null) {
            return "No test fixture being created.";
        }
        ExpectedToFail expectedToFail = fixture.getClass().getAnnotation(ExpectedToFail.class);
        return (expectedToFail != null) ? successValue : "Not Found";
    }

    public String ensureJUnitReportsSuccess(String status) {
        JUnitTestHarnessRunNotifier notifier = junitTestHarness.getNotifier();
        return notifier.wasSuccessful() ? status : "JUnit didn't return a success";
    }

    public String checkConcordionReason(String reason) {
           return reason;
    }

    @After
    public void teardown() {
        System.clearProperty("concordion.extensions");
    }
}
