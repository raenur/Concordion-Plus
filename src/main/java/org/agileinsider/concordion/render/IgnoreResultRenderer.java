package org.agileinsider.concordion.render;

import org.agileinsider.concordion.event.IgnoreEvent;
import org.agileinsider.concordion.event.IgnoreListener;
import org.concordion.api.Element;

public class IgnoreResultRenderer implements IgnoreListener {
    public void ignoredReported(IgnoreEvent event) {
        Element element = event.getElement();
        element.addStyleClass("skip").appendNonBreakingSpaceIfBlank();
    }

}
