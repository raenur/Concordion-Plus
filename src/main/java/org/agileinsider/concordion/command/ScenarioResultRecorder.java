package org.agileinsider.concordion.command;

import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;

import java.util.ArrayList;
import java.util.List;

public class ScenarioResultRecorder implements ResultRecorder {
    private final List<Result> recordedResults = new ArrayList<Result>();

    public void record(Result result) {
        recordedResults.add(result);
    }

    public long getCount(Result result) {
        int count = 0;
        for (Result candidate : recordedResults) {
            if (candidate == result) {
                count++;
            }
        }
        return count;
    }

    public long getExceptionCount() {
        return getCount(Result.EXCEPTION);
    }

    public long getFailureCount() {
        return getCount(Result.FAILURE);
    }
}
