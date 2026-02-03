package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.NormalizedSemantic;
import com.triad.assessment.pipeline.TestOperations;
import com.triad.assessment.trace.NormalizedProcessSignal;
import com.triad.assessment.trace.NormalizedProductIndicator;
import com.triad.assessment.trace.NormalizedSemanticIssue;

class NormalizeIndicatorsStepTest {

    @Test
    void setsNormalizedValuesAndEvidence() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");

        TestOperations operations = new TestOperations();
        operations.productIndicators = List.of(new NormalizedProductIndicator("p1", 0.7, 1.0, false));
        operations.processSignals = List.of(new NormalizedProcessSignal("r1", 0.2, 1.0));
        operations.normalizedSemantic = new NormalizedSemantic(
            List.of(new NormalizedSemanticIssue("s1", 0.5, 1.0)),
            0.8
        );

        NormalizeIndicatorsStep step = new NormalizeIndicatorsStep();
        step.apply(context, operations);

        assertEquals(operations.productIndicators, context.productIndicators());
        assertEquals(operations.processSignals, context.processSignals());
        assertEquals(operations.normalizedSemantic.issues(), context.semanticIssues());
        assertEquals(0.8, context.semanticReliability());
        assertNotNull(context.normalizedEvidence());
        assertEquals(operations.productIndicators, context.normalizedEvidence().productIndicators());
    }
}
