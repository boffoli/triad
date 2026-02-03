package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.TestOperations;

class ValidateInputStepTest {

    @Test
    void throwsWhenEvidenceMissing() {
        AssessmentContext context = new AssessmentContext(null, sampleConfig(), "test");
        ValidateInputStep step = new ValidateInputStep();
        TestOperations operations = new TestOperations();

        assertThrows(IllegalArgumentException.class, () -> step.apply(context, operations));
    }

    @Test
    void throwsWhenConfigMissing() {
        AssessmentContext context = new AssessmentContext(sampleEvidence(), null, "test");
        ValidateInputStep step = new ValidateInputStep();
        TestOperations operations = new TestOperations();

        assertThrows(IllegalArgumentException.class, () -> step.apply(context, operations));
    }

    @Test
    void validatesConfig() {
        AssessmentContext context = new AssessmentContext(sampleEvidence(), sampleConfig(), "test");
        ValidateInputStep step = new ValidateInputStep();
        TestOperations operations = new TestOperations();

        step.apply(context, operations);

        assertTrue(operations.validateCalled);
    }

    private Evidence sampleEvidence() {
        return new Evidence(List.of(), List.of(), null);
    }

    private PedagogicalConfig sampleConfig() {
        return new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
    }
}
