package com.triad.assessment.trace;

public record NormalizedProductIndicator(
    String id,
    double value,
    double weight,
    boolean critical
) {}
