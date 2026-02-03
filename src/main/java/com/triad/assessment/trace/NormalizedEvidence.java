package com.triad.assessment.trace;

import java.util.List;

public record NormalizedEvidence(
    List<NormalizedProductIndicator> productIndicators,
    List<NormalizedProcessSignal> processSignals,
    List<NormalizedSemanticIssue> semanticIssues,
    double semanticReliability
) {}
