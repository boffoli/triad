# Test Report (JUnit)

## Summary
- Status: PASS (0 failures, 0 errors)
- Total runtime: 1043 ms (~1.0s)

## Results by Test Class
### ValidateInputStepTest
- throwsWhenEvidenceMissing
- validatesConfig
- throwsWhenConfigMissing

### BuildResultStepTest
- buildsAssessmentResult

### RenormalizeWeightsStepTest
- usesOperationsWhenEvaluable
- setsNullWeightsWhenNotEvaluable

### BuildContributionsStepTest
- usesOperationsWhenEvaluable
- returnsEmptyWhenNotEvaluable

### AggregateAxesStepTest
- assignsAxisScoresFromOperations

### FuseScoresStepTest
- usesOperationsWhenEvaluable
- setsZeroWhenNotEvaluable

### NormalizeIndicatorsStepTest
- setsNormalizedValuesAndEvidence

### CheckAvailabilityStepTest
- marksNotEvaluableWhenAllMissing
- marksAvailableAxes

### MapGradeStepTest
- usesOperationsWhenEvaluable
- setsZeroWhenNotEvaluable

### TriadApplicationTests
- contextLoads

### AssessmentPipelineTest
- honorsCustomStepOrder

### AssessmentServiceTest
- computesExampleFromDocument
