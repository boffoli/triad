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
            SemanticLog.LOG.info("Fusione punteggi: non eseguibile (assenza evidenze).");
        } else {
            SemanticLog.LOG.info("Fusione punteggi: combinazione degli assi completata.");
            if (TechnicalLog.LOG.isDebugEnabled()) {
                TechnicalLog.LOG.debug("Punteggio globale tecnico: {}", context.globalScore());
            }
        }
    }
}
