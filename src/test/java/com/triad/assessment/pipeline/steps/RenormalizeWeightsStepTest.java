package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;
import com.triad.assessment.trace.EffectiveAxisWeights;

class RenormalizeWeightsStepTest {

    @Test
    void setsNullWeightsWhenNotEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(true);

        RenormalizeWeightsStep step = new RenormalizeWeightsStep();
        step.apply(context, new TestOperations());

        assertNull(context.effectiveWeights().product());
        assertNull(context.effectiveWeights().process());
        assertNull(context.effectiveWeights().semantic());
    }

    @Test
    void usesOperationsWhenEvaluable() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");
        context.setNotEvaluable(false);
        context.setHasProduct(true);
        context.setHasProcess(true);
        context.setHasSemantic(false);

        TestOperations operations = new TestOperations();
        EffectiveAxisWeights expected = new EffectiveAxisWeights(0.5, 0.5, null);
        operations.effectiveAxisWeights = expected;

        RenormalizeWeightsStep step = new RenormalizeWeightsStep();
        step.apply(context, operations);

        assertEquals(expected, context.effectiveWeights());
    }
}
