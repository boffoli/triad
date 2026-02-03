package com.triad.assessment.trace;

import java.util.List;

import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;

public record AssessmentTrace(
    Evidence evidence,
    PedagogicalConfig config,
    NormalizedEvidence normalizedEvidence,
    AxisScores axisScores,
    EffectiveAxisWeights effectiveAxisWeights,
    Double globalScore,
    Integer grade,
    boolean notEvaluable,
    List<Contribution> mainContributors,
    String traceId
) {}
