# Equivalence Class Examples

This folder contains JSON request examples that cover functional equivalence classes
(valid and invalid) for the assessment input domain.

Use these with `POST /api/assessment` or for schema validation.

Valid examples:
- `eq-valid-full.json`
- `eq-valid-product-only.json`
- `eq-valid-process-only.json`
- `eq-valid-semantic-only.json`
- `eq-valid-product-process.json`
- `eq-valid-product-semantic.json`
- `eq-valid-process-semantic.json`
- `eq-valid-boundary-values.json`
- `eq-valid-axis-weight-zero.json`
- `eq-valid-axis-weight-not-normalized.json`
- `eq-valid-cost-direction.json`
- `eq-valid-critical-low.json`
- `eq-valid-semantic-reliability-edges.json`

Invalid examples:
- `eq-invalid-missing-evidence.json`
- `eq-invalid-missing-config.json`
- `eq-invalid-bounds.json`
- `eq-invalid-negative-weight.json`
- `eq-invalid-lambda.json`
- `eq-invalid-maxgrade.json`
- `eq-invalid-missing-direction.json`
- `eq-invalid-semantic-reliability.json`
- `eq-invalid-empty-strings.json`
- `eq-invalid-empty-evidence.json`
