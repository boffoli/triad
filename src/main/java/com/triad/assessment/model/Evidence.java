package com.triad.assessment.model;

import java.util.List;

import jakarta.validation.Valid;

public record Evidence(
    @Valid
    List<ProductIndicator> productIndicators,
    @Valid
    List<ProcessSignal> processSignals,
    @Valid
    SemanticEvidence semanticEvidence
) {}
