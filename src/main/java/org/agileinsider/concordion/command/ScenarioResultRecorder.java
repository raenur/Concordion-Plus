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
