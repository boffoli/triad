package com.triad.assessment.pipeline;

public interface AssessmentStep {
    void apply(AssessmentContext context, AssessmentOperations operations);
}
