package com.triad.assessment.pipeline.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.trace.AxisScores;
import com.triad.assessment.trace.Contribution;
import com.triad.assessment.trace.EffectiveAxisWeights;
import com.triad.assessment.trace.NormalizedEvidence;

class BuildResultStepTest {

    @Test
    void buildsAssessmentResult() {
        Evidence evidence = new Evidence(List.of(), List.of(), null);
        PedagogicalConfig config = new PedagogicalConfig(new AxisWeights(1.0, 1.0, 1.0), 1.0, 30);
        AssessmentContext context = new AssessmentContext(evidence, config, "test");

        context.setNormalizedEvidence(new NormalizedEvidence(List.of(), List.of(), List.of(), 1.0));
        context.setAxisScores(new AxisScores(0.2, 0.3, 0.4));
        context.setEffectiveWeights(new EffectiveAxisWeights(0.4, 0.3, 0.3));
        context.setGlobalScore(0.5);
        context.setGrade(15);
        context.setNotEvaluable(false);
        context.setMainContributors(List.of(new Contribution("PQ", "i1", 0.2)));

        BuildResultStep step = new BuildResultStep();
        step.apply(context, null);

        assertNotNull(context.result());
        assertEquals(15, context.result().grade());
        assertEquals(30, context.result().maxGrade());
        assertEquals(0.5, context.result().trace().globalScore());
        assertEquals("test", context.result().trace().traceId());
    }
}
