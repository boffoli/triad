package com.triad.assessment.pipeline;

import java.util.List;

import com.triad.assessment.trace.NormalizedSemanticIssue;

public record NormalizedSemantic(
    List<NormalizedSemanticIssue> issues,
    double reliability
) {}
