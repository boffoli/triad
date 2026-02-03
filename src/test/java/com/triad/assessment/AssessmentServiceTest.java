package com.triad.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Direction;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.model.ProcessSignal;
import com.triad.assessment.model.ProductIndicator;
import com.triad.assessment.model.SemanticEvidence;
import com.triad.assessment.model.SemanticIssue;
import com.triad.assessment.model.SemanticReliability;
import com.triad.assessment.pipeline.steps.AggregateAxesStep;
import com.triad.assessment.pipeline.steps.BuildContributionsStep;
import com.triad.assessment.pipeline.steps.BuildResultStep;
import com.triad.assessment.pipeline.steps.CheckAvailabilityStep;
import com.triad.assessment.pipeline.steps.FuseScoresStep;
import com.triad.assessment.pipeline.steps.MapGradeStep;
import com.triad.assessment.pipeline.steps.NormalizeIndicatorsStep;
import com.triad.assessment.pipeline.steps.RenormalizeWeightsStep;
import com.triad.assessment.pipeline.steps.ValidateInputStep;
import com.triad.assessment.trace.AssessmentResult;
import com.triad.assessment.trace.AssessmentTrace;
import com.triad.assessment.trace.InMemoryTraceStore;

class AssessmentServiceTest {

    @Test
    void computesExampleFromDocument() {
        Evidence evidence = new Evidence(
            List.of(
                new ProductIndicator("i1", 0.8, Direction.BENEFIT, 0.0, 1.0, 0.6, false),
                new ProductIndicator("i2", 0.6, Direction.BENEFIT, 0.0, 1.0, 0.4, true)
            ),
            List.of(
                new ProcessSignal("r1", 0.3, 0.0, 1.0, 1.0)
            ),
            new SemanticEvidence(
                List.of(
                    new SemanticIssue("s1", 0.5, 0.0, 1.0, 1.0)
                ),
                new SemanticReliability(1.0, 1.0)
            )
        );

        PedagogicalConfig config = new PedagogicalConfig(
            new AxisWeights(0.4, 0.3, 0.3),
            1.0,
            30
        );

        AssessmentService service = new AssessmentService(
            List.of(
                new ValidateInputStep(),
                new NormalizeIndicatorsStep(),
                new AggregateAxesStep(),
                new CheckAvailabilityStep(),
                new RenormalizeWeightsStep(),
                new FuseScoresStep(),
                new MapGradeStep(),
                new BuildContributionsStep(),
                new BuildResultStep()
            ),
            "",
            new InMemoryTraceStore(86400, 1000)
        );
        AssessmentResult result = service.assess(evidence, config);
        AssessmentTrace trace = result.trace();

        assertNotNull(trace.axisScores().productQuality());
        assertNotNull(trace.axisScores().processQuality());
        assertNotNull(trace.axisScores().semanticAlignment());
        assertNotNull(trace.traceId());

        assertEquals(0.432, trace.axisScores().productQuality(), 1e-3);
        assertEquals(0.741, trace.axisScores().processQuality(), 1e-3);
        assertEquals(0.5, trace.axisScores().semanticAlignment(), 1e-6);
        assertEquals(0.545, trace.globalScore(), 1e-3);
        assertEquals(16, result.grade());
        assertEquals(30, result.maxGrade());
    }
}
