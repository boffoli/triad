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
        SemanticLog.LOG.info("Input received: validating evidence and configuration.");
        if (context.evidence() == null) {
            SemanticLog.LOG.info("Invalid input: missing evidence.");
            throw new IllegalArgumentException("evidence is required");
        }
        if (context.config() == null) {
            SemanticLog.LOG.info("Invalid input: missing configuration.");
            throw new IllegalArgumentException("config is required");
        }
        operations.validateConfig(context.config());
        TechnicalLog.LOG.debug("Technical validation completed (traceId={}).", context.traceId());
    }
}
