package com.triad.assessment.pipeline.steps;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.triad.assessment.pipeline.AssessmentContext;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.pipeline.SemanticLog;
import com.triad.assessment.pipeline.TechnicalLog;

@Component
@Order(7)
public final class MapGradeStep implements AssessmentStep {
    @Override
    public void apply(AssessmentContext context, AssessmentOperations operations) {
        int grade = context.notEvaluable()
            ? 0
            : operations.toGrade(context.globalScore(), context.config().maxGrade());
        context.setGrade(grade);
        if (context.notEvaluable()) {
            SemanticLog.LOG.info("Mappatura voto: non eseguibile (assenza evidenze).");
        } else {
            SemanticLog.LOG.info("Mappatura voto: voto finale calcolato.");
            if (TechnicalLog.LOG.isDebugEnabled()) {
                TechnicalLog.LOG.debug("Voto tecnico: {}/{}", context.grade(), context.config().maxGrade());
            }
        }
    }
}
