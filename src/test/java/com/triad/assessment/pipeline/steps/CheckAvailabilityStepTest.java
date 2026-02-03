package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.trace.AxisScores;

class CheckAvailabilityStepTest {

    @Test
    void marksAvailableAxes() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setAxisScores(new AxisScores(0.2, null, 0.4));

        CheckAvailabilityStep step = new CheckAvailabilityStep();
        step.apply(context, null);

        assertTrue(context.hasProduct());
        assertFalse(context.hasProcess());
        assertTrue(context.hasSemantic());
        assertFalse(context.notEvaluable());
    }

    @Test
    void marksNotEvaluableWhenAllMissing() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setAxisScores(new AxisScores(null, null, null));

        CheckAvailabilityStep step = new CheckAvailabilityStep();
        step.apply(context, null);

        assertTrue(context.notEvaluable());
    }
}
