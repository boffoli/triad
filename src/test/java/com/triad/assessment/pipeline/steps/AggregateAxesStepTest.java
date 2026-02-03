package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;
import com.triad.assessment.trace.AxisScores;

class AggregateAxesStepTest {

    @Test
    void assignsAxisScoresFromOperations() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");

        TestOperations operations = new TestOperations();
        operations.axisScores = new AxisScores(0.2, 0.3, 0.4);

        AggregateAxesStep step = new AggregateAxesStep();
        step.apply(context, operations);

        assertEquals(operations.axisScores, context.axisScores());
    }
}
