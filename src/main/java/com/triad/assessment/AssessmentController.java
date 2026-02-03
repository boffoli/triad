package com.triad.assessment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triad.assessment.trace.AssessmentResult;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping
    public ResponseEntity<AssessmentResult> assess(@Valid @RequestBody AssessmentRequest request) {
        AssessmentResult result = assessmentService.assess(request.evidence(), request.config());
        return ResponseEntity.ok(result);
    }
}
