package com.triad.assessment.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.triad.assessment.model.Evidence;
import com.triad.assessment.model.PedagogicalConfig;
import com.triad.assessment.trace.AssessmentResult;

public final class AssessmentPipeline {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentPipeline.class);
    private final AssessmentOperations operations;
    private final List<AssessmentStep> steps;

    public AssessmentPipeline(AssessmentOperations operations, List<AssessmentStep> steps, String stepOrder) {
        this.operations = operations;
        this.steps = orderSteps(steps, stepOrder);
    }

    public AssessmentResult run(Evidence evidence, PedagogicalConfig config) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        try {
            AssessmentContext context = new AssessmentContext(evidence, config, traceId);
            SemanticLog.LOG.info("Assessment started (traceId={}).", traceId);
            for (AssessmentStep step : steps) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pipeline step start: {}", step.getClass().getSimpleName());
                }
                step.apply(context, operations);
                if (logger.isDebugEnabled()) {
                    logger.debug("Pipeline step done: {}", step.getClass().getSimpleName());
                }
            }
            SemanticLog.LOG.info("Assessment finished (traceId={}).", traceId);
            return context.result();
        } finally {
            MDC.remove("traceId");
        }
    }

    private static List<AssessmentStep> orderSteps(List<AssessmentStep> steps, String stepOrder) {
        List<AssessmentStep> ordered = List.copyOf(steps);
        if (stepOrder == null || stepOrder.isBlank()) {
            return ordered;
        }

        Map<String, AssessmentStep> bySimpleName = new LinkedHashMap<>();
        Map<String, AssessmentStep> byClassName = new LinkedHashMap<>();
        for (AssessmentStep step : ordered) {
            String simpleName = step.getClass().getSimpleName();
            if (bySimpleName.putIfAbsent(simpleName, step) != null) {
                throw new IllegalArgumentException("Duplicate step simple name: " + simpleName);
            }
            byClassName.put(step.getClass().getName(), step);
        }

        List<AssessmentStep> result = new ArrayList<>();
        Set<AssessmentStep> used = new HashSet<>();
        for (String raw : stepOrder.split(",")) {
            String name = raw.trim();
            if (name.isEmpty()) {
                continue;
            }
            AssessmentStep step = bySimpleName.get(name);
            if (step == null) {
                step = byClassName.get(name);
            }
            if (step == null) {
                throw new IllegalArgumentException("Unknown pipeline step: " + name);
            }
            if (!used.add(step)) {
                throw new IllegalArgumentException("Duplicate pipeline step: " + name);
            }
            result.add(step);
        }

        if (used.size() != ordered.size()) {
            List<String> missing = new ArrayList<>();
            for (AssessmentStep step : ordered) {
                if (!used.contains(step)) {
                    missing.add(step.getClass().getSimpleName());
                }
            }
            throw new IllegalArgumentException("Pipeline step list is incomplete. Missing: " + missing);
        }

        return Collections.unmodifiableList(result);
    }
}
