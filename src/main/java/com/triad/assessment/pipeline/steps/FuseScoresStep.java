package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;

@Component
@Order(6)
public final class FuseScoresStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        double globalScore = context.notEvaluable()
            ? 0.0
            : operations.computeGlobalScore(context.axisScores(), context.effectiveWeights());
        context.setGlobalScore(globalScore);
        if (context.notEvaluable()) {
            SemanticLog.LOG.info("Score fusion: not executable (missing evidence).");
        } else {
            SemanticLog.LOG.info("Score fusion: axis combination completed.");
            if (TechnicalLog.LOG.isDebugEnabled()) {
                TechnicalLog.LOG.debug("Technical global score: {}", context.globalScore());
            }
        }
    }
}
