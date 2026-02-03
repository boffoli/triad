package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;
import com.triad.assessment.trace.Contribution;

class BuildContributionsStepTest {

    @Test
    void returnsEmptyWhenNotEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(true);

        BuildContributionsStep step = new BuildContributionsStep();
        step.apply(context, new TestOperations());

        assertTrue(context.mainContributors().isEmpty());
    }

    @Test
    void usesOperationsWhenEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(false);

        TestOperations operations = new TestOperations();
        List<Contribution> contributions = List.of(new Contribution("PQ", "i1", 0.2));
        List<Contribution> main = List.of(new Contribution("PQ", "i1", 0.2));
        operations.contributions = contributions;
        operations.mainContributors = main;
        operations.mainContributorsLimit = 3;

        BuildContributionsStep step = new BuildContributionsStep();
        step.apply(context, operations);

        assertEquals(main, context.mainContributors());
        assertEquals(3, operations.selectMainContributorsLimit);
        assertEquals(contributions, operations.selectMainContributorsInput);
    }
}
