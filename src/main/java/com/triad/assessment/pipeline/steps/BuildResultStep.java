package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.trace.AssessmentResult;
import com.triad.assessment.trace.AssessmentTrace;

@Component
@Order(9)
public final class BuildResultStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        AssessmentTrace trace = new AssessmentTrace(
            context.evidence(),
            context.config(),
            context.normalizedEvidence(),
            context.axisScores(),
            context.effectiveWeights(),
            context.globalScore(),
            context.grade(),
            context.notEvaluable(),
            context.mainContributors(),
            context.traceId()
        );
        context.setResult(new AssessmentResult(context.grade(), context.config().maxGrade(), trace));
        SemanticLog.LOG.info("Valutazione completata: risultato disponibile.");
    }
}
