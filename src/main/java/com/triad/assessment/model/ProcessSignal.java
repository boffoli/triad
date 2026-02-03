package com.triad.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProcessSignal(
    @NotBlank(message = "type is required")
    String type,
    double severity,
    double lowerBound,
    double upperBound,
    @PositiveOrZero(message = "weight must be >= 0")
    double weight
) {
    @JsonIgnore
    @AssertTrue(message = "upperBound must be greater than lowerBound")
    public boolean isBoundsValid() {
        return upperBound > lowerBound;
    }
}
