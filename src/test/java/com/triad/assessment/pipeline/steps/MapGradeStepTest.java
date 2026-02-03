package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;

class MapGradeStepTest {

    @Test
    void setsZeroWhenNotEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(true);

        MapGradeStep step = new MapGradeStep();
        step.apply(context, new TestOperations());

        assertEquals(0, context.grade());
    }

    @Test
    void usesOperationsWhenEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(false);

        TestOperations operations = new TestOperations();
        operations.grade = 18;

        MapGradeStep step = new MapGradeStep();
        step.apply(context, operations);

        assertEquals(18, context.grade());
    }
}
