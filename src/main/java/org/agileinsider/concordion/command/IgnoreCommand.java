package org.agileinsider.concordion.command;

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

import org.concordion.api.*;
import org.concordion.internal.util.Announcer;

public class IgnoreCommand extends AbstractCommand {
    private final Announcer<IgnoreListener> listeners = Announcer.to(IgnoreListener.class);

    public void addIgnoreListener(IgnoreListener listener) {
        listeners.addListener(listener);
    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        super.execute(commandCall, evaluator, resultRecorder);
        resultRecorder.record(Result.IGNORED);
        listeners.announce().ignoredReported(new IgnoreEvent(commandCall.getExpression(), commandCall.getElement()));
    }
}
