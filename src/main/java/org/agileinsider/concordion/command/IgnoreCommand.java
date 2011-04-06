package org.agileinsider.concordion.command;

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
