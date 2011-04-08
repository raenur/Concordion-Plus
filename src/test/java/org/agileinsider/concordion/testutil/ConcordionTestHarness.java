package org.agileinsider.concordion.testutil;

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

import org.concordion.Concordion;
import org.concordion.api.Resource;
import org.concordion.internal.ClassNameBasedSpecificationLocator;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.SimpleEvaluatorFactory;

import java.io.IOException;


public class ConcordionTestHarness {
    protected Object fixture = null;

    public ConcordionTestHarness(Object fixture) {
        this.fixture = fixture;
    }

    public ProcessingResult processFragment(String fragment) throws Throwable {
        fragment = "<body><fragment>" + fragment + "</fragment></body>";
        Resource resource = getResource(fixture);
        StubSource stubSource = getSource(fragment, resource);
        StubTarget stubTarget = getTarget();

        process(fixture, resource, stubSource, stubTarget);
        return getProcessingResult(resource, stubTarget);
    }

    protected void process(Object fixture, Resource resource, StubSource stubSource, StubTarget stubTarget) throws Throwable {
        Concordion concordion = new ConcordionBuilder()
                .withSource(stubSource)
                .withEvaluatorFactory(new SimpleEvaluatorFactory())
                .withTarget(stubTarget)
                .build();

        try {
            concordion.process(resource, this.fixture);
        } catch (IOException e) {
            throw new RuntimeException("Test rig failed to process specification", e);
        }
    }

    private ProcessingResult getProcessingResult(Resource resource, StubTarget stubTarget) {
        String xml = stubTarget.getWrittenString(resource);
        return new ProcessingResult(xml);
    }

    private StubTarget getTarget() {
        return new StubTarget();
    }

    private Resource getResource(Object fixture) {
        return new ClassNameBasedSpecificationLocator().locateSpecification(fixture);
    }

    private StubSource getSource(String fragment, Resource resource) {
        StubSource stubSource = new StubSource();
        stubSource.addResource(resource, "<html xmlns:concordion='"
                + ConcordionBuilder.NAMESPACE_CONCORDION_2007 + "'>"
                + fragment
                + "</html>");
        return stubSource;
    }
}
