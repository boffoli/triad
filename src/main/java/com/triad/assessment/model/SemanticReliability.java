package com.triad.assessment.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record SemanticReliability(
    @DecimalMin(value = "0.0", message = "coverage must be >= 0")
    @DecimalMax(value = "1.0", message = "coverage must be <= 1")
    double coverage,
    @DecimalMin(value = "0.0", message = "confidence must be >= 0")
    @DecimalMax(value = "1.0", message = "confidence must be <= 1")
    double confidence
) {}
