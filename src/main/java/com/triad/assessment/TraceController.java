package com.triad.assessment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triad.assessment.trace.AssessmentTrace;
import com.triad.assessment.trace.TraceStore;
import com.triad.assessment.trace.TraceStats;

@RestController
@RequestMapping("/api/trace")
public class TraceController {
    private final TraceStore traceStore;

    public TraceController(TraceStore traceStore) {
        this.traceStore = traceStore;
    }

    @GetMapping("/stats")
    public ResponseEntity<TraceStats> getStats() {
        return ResponseEntity.ok(traceStore.stats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentTrace> getTrace(@PathVariable("id") String id) {
        return traceStore.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearTraces() {
        traceStore.clear();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrace(@PathVariable("id") String id) {
        if (traceStore.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/purge")
    public ResponseEntity<TracePurgeResponse> purgeExpired() {
        int removed = traceStore.purgeExpired();
        return ResponseEntity.ok(new TracePurgeResponse(removed));
    }

    public record TracePurgeResponse(int removed) {}
}
