package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;

class FuseScoresStepTest {

    @Test
    void setsZeroWhenNotEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(true);

        FuseScoresStep step = new FuseScoresStep();
        step.apply(context, new TestOperations());

        assertEquals(0.0, context.globalScore());
    }

    @Test
    void usesOperationsWhenEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(false);

        TestOperations operations = new TestOperations();
        operations.globalScore = 0.42;

        FuseScoresStep step = new FuseScoresStep();
        step.apply(context, operations);

        assertEquals(0.42, context.globalScore());
    }
}
