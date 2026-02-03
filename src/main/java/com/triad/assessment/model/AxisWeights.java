package com.triad.assessment.model;

import jakarta.validation.constraints.PositiveOrZero;

public record AxisWeights(
    @PositiveOrZero(message = "product weight must be >= 0")
    double product,
    @PositiveOrZero(message = "process weight must be >= 0")
    double process,
    @PositiveOrZero(message = "semantic weight must be >= 0")
    double semantic
) {}
