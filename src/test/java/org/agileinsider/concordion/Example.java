package org.agileinsider.concordion;

import org.agileinsider.concordion.junit.ConcordionPlus;
import org.junit.runner.RunWith;

@RunWith(ConcordionPlus.class)
public class Example {
    public String getText() {
        return "success";
    }

    public String getUnexpectedAssertionMessage() {
        return "unexpected message";
    }
}
