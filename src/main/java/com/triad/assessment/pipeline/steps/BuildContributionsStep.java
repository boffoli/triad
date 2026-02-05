package com.triad.assessment.pipeline.steps;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;
import com.triad.assessment.trace.Contribution;

@Component
@Order(8)
public final class BuildContributionsStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        if (context.notEvaluable()) {
            context.setMainContributors(List.of());
            SemanticLog.LOG.info("Contributions: not available (missing evidence).");
            return;
        }

        List<Contribution> contributions = operations.computeContributions(
            context.productIndicators(),
            context.processSignals(),
            context.semanticIssues(),
            context.semanticReliability(),
            context.config(),
            context.effectiveWeights()
        );

        context.setMainContributors(
            operations.selectMainContributors(contributions, operations.mainContributorsLimit())
        );
        SemanticLog.LOG.info("Main contributors: identified {} items.", context.mainContributors().size());
        if (TechnicalLog.LOG.isDebugEnabled()) {
            TechnicalLog.LOG.debug("Technical contributors: {}", context.mainContributors());
        }
    }
}
