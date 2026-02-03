package com.triad.assessment.pipeline;

import java.util.List;

import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.trace.AssessmentResult;
import com.triad.assessment.trace.AxisScores;
import com.triad.assessment.trace.Contribution;
import com.triad.assessment.trace.EffectiveAxisWeights;
import com.triad.assessment.trace.NormalizedEvidence;
import com.triad.assessment.trace.NormalizedProcessSignal;
import com.triad.assessment.trace.NormalizedProductIndicator;
import com.triad.assessment.trace.NormalizedSemanticIssue;

public final class AssessmentContext {
    private final Evidence evidence;
    private final PedagogicalConfig config;
    private final String traceId;

    private List<NormalizedProductIndicator> productIndicators = List.of();
    private List<NormalizedProcessSignal> processSignals = List.of();
    private List<NormalizedSemanticIssue> semanticIssues = List.of();
    private double semanticReliability = 1.0;
    private NormalizedEvidence normalizedEvidence;
    private AxisScores axisScores;
    private boolean hasProduct;
    private boolean hasProcess;
    private boolean hasSemantic;
    private boolean notEvaluable;
    private EffectiveAxisWeights effectiveWeights;
    private double globalScore;
    private int grade;
    private List<Contribution> mainContributors = List.of();
    private AssessmentResult result;

    public AssessmentContext(Evidence evidence, PedagogicalConfig config, String traceId) {
        this.evidence = evidence;
        this.config = config;
        this.traceId = traceId;
    }

    public Evidence evidence() {
        return evidence;
    }

    public PedagogicalConfig config() {
        return config;
    }

    public String traceId() {
        return traceId;
    }

    public List<NormalizedProductIndicator> productIndicators() {
        return productIndicators;
    }

    public void setProductIndicators(List<NormalizedProductIndicator> productIndicators) {
        this.productIndicators = productIndicators;
    }

    public List<NormalizedProcessSignal> processSignals() {
        return processSignals;
    }

    public void setProcessSignals(List<NormalizedProcessSignal> processSignals) {
        this.processSignals = processSignals;
    }

    public List<NormalizedSemanticIssue> semanticIssues() {
        return semanticIssues;
    }

    public void setSemanticIssues(List<NormalizedSemanticIssue> semanticIssues) {
        this.semanticIssues = semanticIssues;
    }

    public double semanticReliability() {
        return semanticReliability;
    }

    public void setSemanticReliability(double semanticReliability) {
        this.semanticReliability = semanticReliability;
    }

    public NormalizedEvidence normalizedEvidence() {
        return normalizedEvidence;
    }

    public void setNormalizedEvidence(NormalizedEvidence normalizedEvidence) {
        this.normalizedEvidence = normalizedEvidence;
    }

    public AxisScores axisScores() {
        return axisScores;
    }

    public void setAxisScores(AxisScores axisScores) {
        this.axisScores = axisScores;
    }

    public boolean hasProduct() {
        return hasProduct;
    }

    public void setHasProduct(boolean hasProduct) {
        this.hasProduct = hasProduct;
    }

    public boolean hasProcess() {
        return hasProcess;
    }

    public void setHasProcess(boolean hasProcess) {
        this.hasProcess = hasProcess;
    }

    public boolean hasSemantic() {
        return hasSemantic;
    }

    public void setHasSemantic(boolean hasSemantic) {
        this.hasSemantic = hasSemantic;
    }

    public boolean notEvaluable() {
        return notEvaluable;
    }

    public void setNotEvaluable(boolean notEvaluable) {
        this.notEvaluable = notEvaluable;
    }

    public EffectiveAxisWeights effectiveWeights() {
        return effectiveWeights;
    }

    public void setEffectiveWeights(EffectiveAxisWeights effectiveWeights) {
        this.effectiveWeights = effectiveWeights;
    }

    public double globalScore() {
        return globalScore;
    }

    public void setGlobalScore(double globalScore) {
        this.globalScore = globalScore;
    }

    public int grade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<Contribution> mainContributors() {
        return mainContributors;
    }

    public void setMainContributors(List<Contribution> mainContributors) {
        this.mainContributors = mainContributors;
    }

    public AssessmentResult result() {
        return result;
    }

    public void setResult(AssessmentResult result) {
        this.result = result;
    }
}
