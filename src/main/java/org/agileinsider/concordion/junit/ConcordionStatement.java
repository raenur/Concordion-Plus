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

import org.agileinsider.concordion.ConcordionPlusExtension;
import org.agileinsider.concordion.IgnoreExtension;
import org.agileinsider.concordion.ScenarioExtension;
import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.command.ScenarioCommand;
import org.agileinsider.concordion.render.IgnoreResultRenderer;
import org.agileinsider.concordion.render.ScenarioResultRenderer;

import org.concordion.Concordion;
import org.concordion.api.*;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.OgnlEvaluatorFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

public class ConcordionStatement extends Statement {
    private static Source fixedSource;
    private static Target fixedTarget;
    private static Resource fixedResource;

    private Class fixtureClass;
    private final Object fixture;
    private RunNotifier notifier;
    private IgnoreCommand ignoreCommand;
    private ScenarioCommand scenarioCommand;

    public ConcordionStatement(Class fixtureClass, Object fixture, RunNotifier notifier) {
        this.fixtureClass = fixtureClass;
        this.fixture = fixture;
        this.notifier = notifier;

        ignoreCommand = new IgnoreCommand();
        scenarioCommand = new ScenarioCommand();
        addIgnoreListeners();
        addScenarioListeners();
    }

    public void evaluate() throws Throwable {
        ConcordionBuilder concordionBuilder = new ConcordionBuilder();
        if (fixture.getClass().isAnnotationPresent(FullOGNL.class)) {
            concordionBuilder.withEvaluatorFactory(new OgnlEvaluatorFactory());
        }
        if (fixedSource != null) {
            concordionBuilder.withSource(fixedSource);
        }
        if (fixedTarget != null) {
            concordionBuilder.withTarget(fixedTarget);
        }
        if (fixedResource != null) {
        }
        concordionBuilder.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE,
                ScenarioExtension.SCENARIO_COMMAND,
                scenarioCommand);
        concordionBuilder.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE,
                IgnoreExtension.IGNORE_COMMAND,
                ignoreCommand);
        Concordion concordion = concordionBuilder.build();
        ResultSummary resultSummary = concordion.process(fixture);
        resultSummary.print(System.out, fixture);
        resultSummary.assertIsSatisfied(fixture);
    }

    private void addScenarioListeners() {
        scenarioCommand.addScenarioListener(new ScenarioResultRenderer());
        scenarioCommand.addScenarioListener(new ScenarioNotifier(new NotifierFactory(notifier, fixtureClass)));
    }

    private void addIgnoreListeners() {
        ignoreCommand.addIgnoreListener(new IgnoreResultRenderer());
        ignoreCommand.addIgnoreListener(new IgnoreNotifier(new NotifierFactory(notifier, fixtureClass)));
    }

    public static void setSource(Source fixedSource) {
        ConcordionStatement.fixedSource = fixedSource;
    }

    public static void setTarget(Target fixedTarget) {
        ConcordionStatement.fixedTarget = fixedTarget;
    }

    public static void setResource(Resource fixedResource) {
        ConcordionStatement.fixedResource = fixedResource;
    }
}
