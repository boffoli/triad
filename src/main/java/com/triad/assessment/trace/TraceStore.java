package com.triad.assessment.trace;

import java.util.Optional;

public interface TraceStore {
    void save(AssessmentTrace trace);

    Optional<AssessmentTrace> findById(String traceId);

    TraceStats stats();

    void clear();

    boolean deleteById(String traceId);

    int purgeExpired();
}
