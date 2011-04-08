package org.agileinsider.concordion.junit;

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

import org.agileinsider.concordion.event.IgnoreEvent;
import org.agileinsider.concordion.event.IgnoreListener;

import org.junit.internal.runners.model.EachTestNotifier;

public class IgnoreNotifier implements IgnoreListener {
    private final NotifierFactory notifierFactory;

    public IgnoreNotifier(NotifierFactory notifierFactory) {
        this.notifierFactory = notifierFactory;
    }

    public void ignoredReported(IgnoreEvent event) {
        EachTestNotifier testNotifier = notifierFactory.createNotifier(event);
        testNotifier.fireTestIgnored();
    }
}
