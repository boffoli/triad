package com.triad.assessment.pipeline;

import java.util.List;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.trace.AxisScores;
import com.triad.assessment.trace.Contribution;
import com.triad.assessment.trace.EffectiveAxisWeights;
import com.triad.assessment.trace.NormalizedProcessSignal;
import com.triad.assessment.trace.NormalizedProductIndicator;
import com.triad.assessment.trace.NormalizedSemanticIssue;

public interface AssessmentOperations {
    void validateConfig(PedagogicalConfig config);

    List<NormalizedProductIndicator> normalizeProducts(Evidence evidence);

    List<NormalizedProcessSignal> normalizeProcess(Evidence evidence);

    NormalizedSemantic normalizeSemantic(Evidence evidence);

    AxisScores computeAxisScores(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config
    );

    EffectiveAxisWeights computeEffectiveWeights(
        AxisWeights weights,
        boolean hasProduct,
        boolean hasProcess,
        boolean hasSemantic
    );

    double computeGlobalScore(AxisScores scores, EffectiveAxisWeights weights);

    int toGrade(double globalScore, int maxGrade);

    List<Contribution> computeContributions(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config,
        EffectiveAxisWeights effectiveWeights
    );

    List<Contribution> selectMainContributors(List<Contribution> contributions, int limit);

    int mainContributorsLimit();
}
