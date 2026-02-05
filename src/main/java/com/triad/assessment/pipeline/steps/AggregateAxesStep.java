package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;

@Component
@Order(3)
public final class AggregateAxesStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        context.setAxisScores(operations.computeAxisScores(
            context.productIndicators(),
            context.processSignals(),
            context.semanticIssues(),
            context.semanticReliability(),
            context.config()
        ));
        SemanticLog.LOG.info("Axis aggregation: scores computed for product, process, and semantic.");
        if (TechnicalLog.LOG.isDebugEnabled()) {
            TechnicalLog.LOG.debug(
                "Technical scores: PQ={}, PrQ={}, SA={}",
                context.axisScores().productQuality(),
                context.axisScores().processQuality(),
                context.axisScores().semanticAlignment()
            );
        }
    }
}
