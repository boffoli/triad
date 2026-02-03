package com.triad.assessment;

import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AssessmentRequest(
    @NotNull(message = "evidence is required")
    @Valid
    Evidence evidence,
    @NotNull(message = "config is required")
    @Valid
    PedagogicalConfig config
) {}
