package com.triad.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductIndicator(
    @NotBlank(message = "id is required")
    String id,
    double value,
    @NotNull(message = "direction is required")
    Direction direction,
    double lowerBound,
    double upperBound,
    @PositiveOrZero(message = "weight must be >= 0")
    double weight,
    boolean critical
) {
    @JsonIgnore
    @AssertTrue(message = "upperBound must be greater than lowerBound")
    public boolean isBoundsValid() {
        return upperBound > lowerBound;
    }
}
