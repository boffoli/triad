package com.triad.assessment.model;

import java.util.List;

import jakarta.validation.Valid;

public record SemanticEvidence(
    @Valid
    List<SemanticIssue> issues,
    @Valid
    SemanticReliability reliability
) {}
