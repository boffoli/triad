package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;

@Component
@Order(1)
public final class ValidateInputStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        SemanticLog.LOG.info("Input ricevuto: avvio validazione evidenze e configurazione.");
        if (context.evidence() == null) {
            SemanticLog.LOG.info("Input non valido: evidenze mancanti.");
            throw new IllegalArgumentException("evidence is required");
        }
        if (context.config() == null) {
            SemanticLog.LOG.info("Input non valido: configurazione mancante.");
            throw new IllegalArgumentException("config is required");
        }
        operations.validateConfig(context.config());
        TechnicalLog.LOG.debug("Validazione tecnica completata (traceId={}).", context.traceId());
    }
}
