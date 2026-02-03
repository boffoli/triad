package com.triad.assessment.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;

class AssessmentPipelineTest {

    @Test
    void honorsCustomStepOrder() {
        List<String> calls = new ArrayList<>();
        List<AssessmentStep> steps = List.of(
            new FirstStep(calls),
            new SecondStep(calls),
            new ThirdStep(calls)
        );

        AssessmentPipeline pipeline = new AssessmentPipeline(
            new TestOperations(),
            steps,
            "ThirdStep,FirstStep,SecondStep"
        );

        pipeline.run(
            new Evidence(List.of(), List.of(), null),
            new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30)
        );

        assertEquals(List.of("ThirdStep", "FirstStep", "SecondStep"), calls);
    }

    private static final class FirstStep implements AssessmentStep {
        private final List<String> calls;

        private FirstStep(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void apply(AssessmentContext context, AssessmentOperations operations) {
            calls.add(getClass().getSimpleName());
        }
    }

    private static final class SecondStep implements AssessmentStep {
        private final List<String> calls;

        private SecondStep(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void apply(AssessmentContext context, AssessmentOperations operations) {
            calls.add(getClass().getSimpleName());
        }
    }

    private static final class ThirdStep implements AssessmentStep {
        private final List<String> calls;

        private ThirdStep(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void apply(AssessmentContext context, AssessmentOperations operations) {
            calls.add(getClass().getSimpleName());
        }
    }
}
