package org.agileinsider.concordion.testutil;

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
