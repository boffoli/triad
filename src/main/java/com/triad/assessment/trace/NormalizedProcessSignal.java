package com.triad.assessment.trace;

public record NormalizedProcessSignal(
    String type,
    double severity,
    double weight
) {}
