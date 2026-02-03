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

public final class TestOperations implements AssessmentOperations {
    public boolean validateCalled;
    public List<NormalizedProductIndicator> productIndicators = List.of();
    public List<NormalizedProcessSignal> processSignals = List.of();
    public NormalizedSemantic normalizedSemantic = new NormalizedSemantic(List.of(), 1.0);
    public AxisScores axisScores = new AxisScores(null, null, null);
    public EffectiveAxisWeights effectiveAxisWeights = new EffectiveAxisWeights(null, null, null);
    public double globalScore;
    public int grade;
    public List<Contribution> contributions = List.of();
    public List<Contribution> mainContributors = List.of();
    public int mainContributorsLimit = 5;
    public int selectMainContributorsLimit = -1;
    public List<Contribution> selectMainContributorsInput = List.of();

    @Override
    public void validateConfig(PedagogicalConfig config) {
        validateCalled = true;
    }

    @Override
    public List<NormalizedProductIndicator> normalizeProducts(Evidence evidence) {
        return productIndicators;
    }

    @Override
    public List<NormalizedProcessSignal> normalizeProcess(Evidence evidence) {
        return processSignals;
    }

    @Override
    public NormalizedSemantic normalizeSemantic(Evidence evidence) {
        return normalizedSemantic;
    }

    @Override
    public AxisScores computeAxisScores(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config
    ) {
        return axisScores;
    }

    @Override
    public EffectiveAxisWeights computeEffectiveWeights(
        AxisWeights weights,
        boolean hasProduct,
        boolean hasProcess,
        boolean hasSemantic
    ) {
        return effectiveAxisWeights;
    }

    @Override
    public double computeGlobalScore(AxisScores scores, EffectiveAxisWeights weights) {
        return globalScore;
    }

    @Override
    public int toGrade(double globalScore, int maxGrade) {
        return grade;
    }

    @Override
    public List<Contribution> computeContributions(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config,
        EffectiveAxisWeights effectiveAxisWeights
    ) {
        return contributions;
    }

    @Override
    public List<Contribution> selectMainContributors(List<Contribution> contributions, int limit) {
        selectMainContributorsInput = contributions;
        selectMainContributorsLimit = limit;
        return mainContributors;
    }

    @Override
    public int mainContributorsLimit() {
        return mainContributorsLimit;
    }
}
