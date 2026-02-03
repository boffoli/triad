package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.NormalizedSemantic;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;
import com.triad.assessment.trace.NormalizedEvidence;

@Component
@Order(2)
public final class NormalizeIndicatorsStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        SemanticLog.LOG.info("Normalizzazione: indicatori resi confrontabili in scala 0-1.");
        context.setProductIndicators(operations.normalizeProducts(context.evidence()));
        context.setProcessSignals(operations.normalizeProcess(context.evidence()));
        NormalizedSemantic normalizedSemantic = operations.normalizeSemantic(context.evidence());
        context.setSemanticIssues(normalizedSemantic.issues());
        context.setSemanticReliability(normalizedSemantic.reliability());
        context.setNormalizedEvidence(new NormalizedEvidence(
            context.productIndicators(),
            context.processSignals(),
            context.semanticIssues(),
            context.semanticReliability()
        ));
        if (TechnicalLog.LOG.isDebugEnabled()) {
            TechnicalLog.LOG.debug(
                "Normalizzazione tecnica: prodotto={}, processo={}, semantica={}, affidabilitaSem={}",
                context.productIndicators().size(),
                context.processSignals().size(),
                context.semanticIssues().size(),
                context.semanticReliability()
            );
        }
    }
}
