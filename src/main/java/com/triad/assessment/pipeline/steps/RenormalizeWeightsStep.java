package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;
import com.triad.assessment.trace.EffectiveAxisWeights;

@Component
@Order(5)
public final class RenormalizeWeightsStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        if (context.notEvaluable()) {
            context.setEffectiveWeights(new EffectiveAxisWeights(null, null, null));
            SemanticLog.LOG.info("Axis weights: not applicable (evaluation not executable).");
            return;
        }

        context.setEffectiveWeights(operations.computeEffectiveWeights(
            context.config().axisWeights(),
            context.hasProduct(),
            context.hasProcess(),
            context.hasSemantic()
        ));
        SemanticLog.LOG.info("Axis weights: rebalanced on available axes.");
        if (TechnicalLog.LOG.isDebugEnabled()) {
            TechnicalLog.LOG.debug("Technical effective weights: {}", context.effectiveWeights());
        }
    }
}
