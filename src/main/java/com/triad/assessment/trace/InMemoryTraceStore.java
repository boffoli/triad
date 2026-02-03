package com.triad.assessment.trace;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component
public class InMemoryTraceStore implements TraceStore {
    private final Map<String, StoredTrace> traces = new LinkedHashMap<>();
    private final long ttlMillis;
    private final long ttlSeconds;
    private final int maxSize;

    public InMemoryTraceStore(
        @Value("${assessment.trace.ttl-seconds:86400}") long ttlSeconds,
        @Value("${assessment.trace.max-size:1000}") int maxSize
    ) {
        this.ttlSeconds = ttlSeconds;
        this.ttlMillis = ttlSeconds <= 0 ? 0 : ttlSeconds * 1000;
        this.maxSize = Math.max(1, maxSize);
    }

    @Override
    public void save(AssessmentTrace trace) {
        if (trace == null || trace.traceId() == null || trace.traceId().isBlank()) {
            return;
        }
        long now = System.currentTimeMillis();
        synchronized (traces) {
            cleanupExpired(now);
            traces.put(trace.traceId(), new StoredTrace(trace, now));
            trimToMaxSize();
        }
    }

    @Override
    public Optional<AssessmentTrace> findById(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            return Optional.empty();
        }
        long now = System.currentTimeMillis();
        synchronized (traces) {
            cleanupExpired(now);
            StoredTrace stored = traces.get(traceId);
            if (stored == null) {
                return Optional.empty();
            }
            if (isExpired(stored, now)) {
                traces.remove(traceId);
                return Optional.empty();
            }
            return Optional.of(stored.trace());
        }
    }

    @Override
    public TraceStats stats() {
        synchronized (traces) {
            cleanupExpired(System.currentTimeMillis());
            return new TraceStats(traces.size(), maxSize, ttlSeconds);
        }
    }

    @Override
    public void clear() {
        synchronized (traces) {
            traces.clear();
        }
    }

    @Override
    public boolean deleteById(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            return false;
        }
        synchronized (traces) {
            return traces.remove(traceId) != null;
        }
    }

    @Override
    public int purgeExpired() {
        if (ttlMillis <= 0) {
            return 0;
        }
        int removed = 0;
        long now = System.currentTimeMillis();
        synchronized (traces) {
            Iterator<Map.Entry<String, StoredTrace>> iterator = traces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, StoredTrace> entry = iterator.next();
                if (isExpired(entry.getValue(), now)) {
                    iterator.remove();
                    removed++;
                }
            }
        }
        return removed;
    }

    private void cleanupExpired(long now) {
        if (ttlMillis <= 0) {
            return;
        }
        Iterator<Map.Entry<String, StoredTrace>> iterator = traces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, StoredTrace> entry = iterator.next();
            if (isExpired(entry.getValue(), now)) {
                iterator.remove();
            }
        }
    }

    private boolean isExpired(StoredTrace stored, long now) {
        return ttlMillis > 0 && now - stored.createdAtMillis() > ttlMillis;
    }

    private void trimToMaxSize() {
        while (traces.size() > maxSize) {
            Iterator<String> iterator = traces.keySet().iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            } else {
                break;
            }
        }
    }

    private record StoredTrace(AssessmentTrace trace, long createdAtMillis) {}
}
