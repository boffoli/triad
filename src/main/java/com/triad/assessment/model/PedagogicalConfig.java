package com.triad.assessment.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedagogicalConfig(
    @NotNull(message = "axisWeights are required")
    @Valid
    AxisWeights axisWeights,
    @Positive(message = "lambda must be > 0")
    double lambda,
    @Positive(message = "maxGrade must be > 0")
    int maxGrade
) {}
