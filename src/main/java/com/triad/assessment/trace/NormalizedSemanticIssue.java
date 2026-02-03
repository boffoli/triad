package com.triad.assessment.trace;

public record NormalizedSemanticIssue(
    String category,
    double severity,
    double weight
) {}
