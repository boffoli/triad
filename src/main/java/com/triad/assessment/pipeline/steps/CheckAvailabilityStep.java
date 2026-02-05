package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;

@Component
@Order(4)
public final class CheckAvailabilityStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        boolean hasProduct = context.axisScores().productQuality() != null;
        boolean hasProcess = context.axisScores().processQuality() != null;
        boolean hasSemantic = context.axisScores().semanticAlignment() != null;

        context.setHasProduct(hasProduct);
        context.setHasProcess(hasProcess);
        context.setHasSemantic(hasSemantic);
        context.setNotEvaluable(!hasProduct && !hasProcess && !hasSemantic);
        if (context.notEvaluable()) {
            SemanticLog.LOG.info("Evidence availability: no axes available, evaluation not possible.");
        } else {
            SemanticLog.LOG.info("Evidence availability: available axes -> product={}, process={}, semantic={}.",
                hasProduct, hasProcess, hasSemantic);
        }
    }
}
