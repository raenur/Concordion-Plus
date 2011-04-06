package org.agileinsider.concordion.event;

import java.util.EventListener;

public interface IgnoreListener extends EventListener {
    void ignoredReported(IgnoreEvent ignoreEvent);
}
