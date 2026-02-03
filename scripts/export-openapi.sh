#!/usr/bin/env bash
set -euo pipefail

OPENAPI_URL="${OPENAPI_URL:-http://localhost:8080/v3/api-docs.yaml}"
OUT_FILE="${OUT_FILE:-src/main/resources/static/openapi/triad.openapi.yaml}"

curl -fsSL "$OPENAPI_URL" -o "$OUT_FILE"

echo "OpenAPI exported to $OUT_FILE"
