package com.triad.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record SemanticIssue(
    @NotBlank(message = "category is required")
    String category,
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
