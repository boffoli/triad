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
            SemanticLog.LOG.info("Pesi assi: non applicabili (valutazione non eseguibile).");
            return;
        }

        context.setEffectiveWeights(operations.computeEffectiveWeights(
            context.config().axisWeights(),
            context.hasProduct(),
            context.hasProcess(),
            context.hasSemantic()
        ));
        SemanticLog.LOG.info("Pesi assi: ricalibrati sugli assi disponibili.");
        if (TechnicalLog.LOG.isDebugEnabled()) {
            TechnicalLog.LOG.debug("Pesi tecnici effettivi: {}", context.effectiveWeights());
        }
    }
}
