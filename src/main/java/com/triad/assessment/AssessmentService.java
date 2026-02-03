package com.triad.assessment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.triad.assessment.model.AxisWeights;
import com.triad.assessment.model.Direction;
import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.model.ProcessSignal;
import com.triad.assessment.model.ProductIndicator;
import com.triad.assessment.model.SemanticEvidence;
import com.triad.assessment.model.SemanticIssue;
import com.triad.assessment.model.SemanticReliability;
import com.triad.assessment.pipeline.AssessmentOperations;
import com.triad.assessment.pipeline.AssessmentPipeline;
import com.triad.assessment.pipeline.NormalizedSemantic;
import com.triad.assessment.pipeline.AssessmentStep;
import com.triad.assessment.trace.AssessmentResult;
import com.triad.assessment.trace.AxisScores;
import com.triad.assessment.trace.Contribution;
import com.triad.assessment.trace.EffectiveAxisWeights;
import com.triad.assessment.trace.NormalizedProcessSignal;
import com.triad.assessment.trace.NormalizedProductIndicator;
import com.triad.assessment.trace.NormalizedSemanticIssue;
import com.triad.assessment.trace.TraceStore;

@Service
public class AssessmentService implements AssessmentOperations {

    static final int MAIN_CONTRIBUTORS_LIMIT = 5;
    private final AssessmentPipeline pipeline;
    private final TraceStore traceStore;

    public AssessmentService(
        List<AssessmentStep> steps,
        @Value("${assessment.pipeline.steps:}") String stepOrder,
        TraceStore traceStore
    ) {
        this.pipeline = new AssessmentPipeline(this, steps, stepOrder);
        this.traceStore = traceStore;
    }

    public AssessmentResult assess(Evidence evidence, PedagogicalConfig config) {
        AssessmentResult result = pipeline.run(evidence, config);
        traceStore.save(result.trace());
        return result;
    }

    @Override
    public void validateConfig(PedagogicalConfig config) {
        AxisWeights weights = config.axisWeights();
        if (weights == null) {
            throw new IllegalArgumentException("axisWeights are required");
        }
        if (weights.product() < 0 || weights.process() < 0 || weights.semantic() < 0) {
            throw new IllegalArgumentException("axisWeights must be non-negative");
        }
        if (config.lambda() <= 0) {
            throw new IllegalArgumentException("lambda must be > 0");
        }
        if (config.maxGrade() <= 0) {
            throw new IllegalArgumentException("maxGrade must be > 0");
        }
    }

    @Override
    public List<NormalizedProductIndicator> normalizeProducts(Evidence evidence) {
        List<ProductIndicator> inputs = safeList(evidence.productIndicators());
        if (inputs.isEmpty()) {
            return List.of();
        }
        List<NormalizedProductIndicator> normalized = new ArrayList<>();
        for (ProductIndicator indicator : inputs) {
            validateBounds(indicator.lowerBound(), indicator.upperBound(), indicator.id());
            if (indicator.weight() < 0) {
                throw new IllegalArgumentException("weight must be non-negative for product indicator: " + indicator.id());
            }
            double n = clip((indicator.value() - indicator.lowerBound()) / (indicator.upperBound() - indicator.lowerBound()));
            double vHat = indicator.direction() == Direction.BENEFIT ? n : 1.0 - n;
            normalized.add(new NormalizedProductIndicator(
                indicator.id(),
                vHat,
                indicator.weight(),
                indicator.critical()
            ));
        }
        return normalized;
    }

    @Override
    public List<NormalizedProcessSignal> normalizeProcess(Evidence evidence) {
        List<ProcessSignal> inputs = safeList(evidence.processSignals());
        if (inputs.isEmpty()) {
            return List.of();
        }
        List<NormalizedProcessSignal> normalized = new ArrayList<>();
        for (ProcessSignal signal : inputs) {
            validateBounds(signal.lowerBound(), signal.upperBound(), signal.type());
            if (signal.weight() < 0) {
                throw new IllegalArgumentException("weight must be non-negative for process signal: " + signal.type());
            }
            double sHat = clip((signal.severity() - signal.lowerBound()) / (signal.upperBound() - signal.lowerBound()));
            normalized.add(new NormalizedProcessSignal(
                signal.type(),
                sHat,
                signal.weight()
            ));
        }
        return normalized;
    }

    @Override
    public NormalizedSemantic normalizeSemantic(Evidence evidence) {
        SemanticEvidence semanticEvidence = evidence.semanticEvidence();
        if (semanticEvidence == null) {
            return new NormalizedSemantic(List.of(), 1.0);
        }
        List<SemanticIssue> issues = safeList(semanticEvidence.issues());
        List<NormalizedSemanticIssue> normalized = new ArrayList<>();
        for (SemanticIssue issue : issues) {
            validateBounds(issue.lowerBound(), issue.upperBound(), issue.category());
            if (issue.weight() < 0) {
                throw new IllegalArgumentException("weight must be non-negative for semantic issue: " + issue.category());
            }
            double qHat = clip((issue.severity() - issue.lowerBound()) / (issue.upperBound() - issue.lowerBound()));
            normalized.add(new NormalizedSemanticIssue(
                issue.category(),
                qHat,
                issue.weight()
            ));
        }

        SemanticReliability reliability = semanticEvidence.reliability();
        double coverage = reliability != null ? reliability.coverage() : 1.0;
        double confidence = reliability != null ? reliability.confidence() : 1.0;
        double r = clip(coverage) * clip(confidence);

        return new NormalizedSemantic(normalized, r);
    }

    @Override
    public AxisScores computeAxisScores(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config
    ) {
        Double productScore = computeProductQuality(productIndicators);
        Double processScore = computeProcessQuality(processSignals, config.lambda());
        Double semanticScore = computeSemanticAlignment(semanticIssues, semanticReliability);

        return new AxisScores(productScore, processScore, semanticScore);
    }

    private Double computeProductQuality(List<NormalizedProductIndicator> productIndicators) {
        if (productIndicators.isEmpty()) {
            return null;
        }
        double totalWeight = sumWeights(productIndicators);
        if (totalWeight <= 0) {
            return null;
        }
        double weightedSum = 0.0;
        for (NormalizedProductIndicator indicator : productIndicators) {
            weightedSum += indicator.weight() * indicator.value();
        }
        double base = weightedSum / totalWeight;

        double criticalPenalty = 1.0;
        boolean hasCritical = false;
        for (NormalizedProductIndicator indicator : productIndicators) {
            if (indicator.critical()) {
                hasCritical = true;
                criticalPenalty *= indicator.value();
            }
        }
        if (!hasCritical) {
            criticalPenalty = 1.0;
        }

        return base * criticalPenalty;
    }

    private Double computeProcessQuality(List<NormalizedProcessSignal> processSignals, double lambda) {
        if (processSignals.isEmpty()) {
            return null;
        }
        double cumulativePenalty = 0.0;
        for (NormalizedProcessSignal signal : processSignals) {
            cumulativePenalty += signal.weight() * signal.severity();
        }
        return Math.exp(-lambda * cumulativePenalty);
    }

    private Double computeSemanticAlignment(List<NormalizedSemanticIssue> semanticIssues, double reliability) {
        if (semanticIssues.isEmpty()) {
            return null;
        }
        double totalWeight = sumWeightsSemantic(semanticIssues);
        if (totalWeight <= 0) {
            return null;
        }
        double weightedSum = 0.0;
        for (NormalizedSemanticIssue issue : semanticIssues) {
            weightedSum += issue.weight() * issue.severity();
        }
        double csa = weightedSum / totalWeight;
        double adjustedPenalty = reliability * csa;
        return 1.0 - adjustedPenalty;
    }

    @Override
    public EffectiveAxisWeights computeEffectiveWeights(
        AxisWeights weights,
        boolean hasProduct,
        boolean hasProcess,
        boolean hasSemantic
    ) {
        double sum = 0.0;
        if (hasProduct) {
            sum += weights.product();
        }
        if (hasProcess) {
            sum += weights.process();
        }
        if (hasSemantic) {
            sum += weights.semantic();
        }

        if (sum <= 0) {
            int count = 0;
            if (hasProduct) {
                count++;
            }
            if (hasProcess) {
                count++;
            }
            if (hasSemantic) {
                count++;
            }
            double equal = count > 0 ? 1.0 / count : 0.0;
            return new EffectiveAxisWeights(
                hasProduct ? equal : null,
                hasProcess ? equal : null,
                hasSemantic ? equal : null
            );
        }

        return new EffectiveAxisWeights(
            hasProduct ? weights.product() / sum : null,
            hasProcess ? weights.process() / sum : null,
            hasSemantic ? weights.semantic() / sum : null
        );
    }

    @Override
    public double computeGlobalScore(AxisScores scores, EffectiveAxisWeights weights) {
        double global = 0.0;
        if (scores.productQuality() != null && weights.product() != null) {
            global += weights.product() * scores.productQuality();
        }
        if (scores.processQuality() != null && weights.process() != null) {
            global += weights.process() * scores.processQuality();
        }
        if (scores.semanticAlignment() != null && weights.semantic() != null) {
            global += weights.semantic() * scores.semanticAlignment();
        }
        return clip(global);
    }

    @Override
    public int toGrade(double globalScore, int maxGrade) {
        long raw = Math.round(maxGrade * globalScore);
        if (raw < 0) {
            return 0;
        }
        if (raw > maxGrade) {
            return maxGrade;
        }
        return (int) raw;
    }

    @Override
    public List<Contribution> computeContributions(
        List<NormalizedProductIndicator> productIndicators,
        List<NormalizedProcessSignal> processSignals,
        List<NormalizedSemanticIssue> semanticIssues,
        double semanticReliability,
        PedagogicalConfig config,
        EffectiveAxisWeights effectiveWeights
    ) {
        List<Contribution> contributions = new ArrayList<>();
        if (effectiveWeights.product() != null && !productIndicators.isEmpty()) {
            double totalWeight = sumWeights(productIndicators);
            if (totalWeight > 0) {
                for (NormalizedProductIndicator indicator : productIndicators) {
                    double base = (indicator.weight() * indicator.value()) / totalWeight;
                    double delta = indicator.critical()
                        ? base - (1.0 - indicator.value())
                        : base;
                    contributions.add(new Contribution(
                        "PQ",
                        indicator.id(),
                        effectiveWeights.product() * delta
                    ));
                }
            }
        }

        if (effectiveWeights.process() != null && !processSignals.isEmpty()) {
            for (NormalizedProcessSignal signal : processSignals) {
                double delta = -config.lambda() * signal.weight() * signal.severity();
                contributions.add(new Contribution(
                    "PrQ",
                    signal.type(),
                    effectiveWeights.process() * delta
                ));
            }
        }

        if (effectiveWeights.semantic() != null && !semanticIssues.isEmpty()) {
            double totalWeight = sumWeightsSemantic(semanticIssues);
            if (totalWeight > 0) {
                for (NormalizedSemanticIssue issue : semanticIssues) {
                    double delta = -semanticReliability * (issue.weight() * issue.severity()) / totalWeight;
                    contributions.add(new Contribution(
                        "SA",
                        issue.category(),
                        effectiveWeights.semantic() * delta
                    ));
                }
            }
        }

        return contributions;
    }

    @Override
    public List<Contribution> selectMainContributors(List<Contribution> contributions, int limit) {
        if (contributions.isEmpty()) {
            return List.of();
        }
        List<Contribution> sorted = new ArrayList<>(contributions);
        sorted.sort(Comparator.comparingDouble(c -> -Math.abs(c.value())));
        if (sorted.size() <= limit) {
            return Collections.unmodifiableList(sorted);
        }
        return Collections.unmodifiableList(sorted.subList(0, limit));
    }

    @Override
    public int mainContributorsLimit() {
        return MAIN_CONTRIBUTORS_LIMIT;
    }

    private double sumWeights(List<NormalizedProductIndicator> indicators) {
        double total = 0.0;
        for (NormalizedProductIndicator indicator : indicators) {
            total += indicator.weight();
        }
        return total;
    }

    private double sumWeightsSemantic(List<NormalizedSemanticIssue> issues) {
        double total = 0.0;
        for (NormalizedSemanticIssue issue : issues) {
            total += issue.weight();
        }
        return total;
    }

    private void validateBounds(double lower, double upper, String id) {
        if (upper <= lower) {
            throw new IllegalArgumentException("upperBound must be greater than lowerBound for: " + id);
        }
    }

    private double clip(double value) {
        return Math.min(1.0, Math.max(0.0, value));
    }

    private <T> List<T> safeList(List<T> list) {
        return list == null ? List.of() : list;
    }

}
