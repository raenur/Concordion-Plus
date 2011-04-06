package org.agileinsider.concordion.testutil;

import org.concordion.Concordion;
import org.concordion.api.EvaluatorFactory;
import org.concordion.api.Resource;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.SimpleEvaluatorFactory;

import java.io.IOException;


public class ConcordionTestHarness {

    private Object fixture = null;
    private EvaluatorFactory evaluatorFactory = new SimpleEvaluatorFactory();
    private StubSource stubSource = new StubSource();

    public ConcordionTestHarness(Object fixture) {
        this.fixture = fixture;
    }

    public ProcessingResult processFragment(String fragment) {
        fragment = "<body><fragment>" + fragment + "</fragment></body>";
        Resource resource = new Resource("/testrig");
        stubSource.addResource(resource, "<html xmlns:concordion='"
                + ConcordionBuilder.NAMESPACE_CONCORDION_2007 + "'>"
                + fragment
                + "</html>");
        StubTarget stubTarget = new StubTarget();
        Concordion concordion = new ConcordionBuilder()
                .withSource(stubSource)
                .withEvaluatorFactory(evaluatorFactory)
                .withTarget(stubTarget)
                .build();

        try {
            concordion.process(resource, fixture);
            String xml = stubTarget.getWrittenString(resource);
            return new ProcessingResult(xml);
        } catch (IOException e) {
            throw new RuntimeException("Test rig failed to process specification", e);
        }
    }
}
