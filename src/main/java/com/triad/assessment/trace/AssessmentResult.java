package com.triad.assessment.trace;

public record AssessmentResult(
    int grade,
    int maxGrade,
    AssessmentTrace trace
) {}
