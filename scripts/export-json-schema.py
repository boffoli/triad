#!/usr/bin/env python3
import json
import os
import sys
from pathlib import Path
from urllib.request import urlopen

OPENAPI_URL = os.environ.get("OPENAPI_URL", "http://localhost:8080/v3/api-docs")
OUTPUT_DIR = Path(os.environ.get("SCHEMA_OUT_DIR", "src/main/resources/static/schema/generated"))


def convert_nullable(node):
    if isinstance(node, dict):
        # Convert OpenAPI 'nullable' to JSON Schema union with null
        if node.get("nullable") is True and "type" in node:
            t = node["type"]
            if isinstance(t, list):
                if "null" not in t:
                    node["type"] = t + ["null"]
            else:
                node["type"] = [t, "null"]
            node.pop("nullable", None)
        # Recurse
        for key, value in list(node.items()):
            convert_nullable(value)
    elif isinstance(node, list):
        for item in node:
            convert_nullable(item)


def main():
    try:
        with urlopen(OPENAPI_URL) as resp:
            data = json.load(resp)
    except Exception as exc:
        print(f"Failed to read OpenAPI JSON from {OPENAPI_URL}: {exc}", file=sys.stderr)
        return 1

    schemas = data.get("components", {}).get("schemas", {})
    if not schemas:
        print("No components.schemas found in OpenAPI document.", file=sys.stderr)
        return 1

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    for name, schema in schemas.items():
        schema_doc = {
            "$schema": "https://json-schema.org/draft/2020-12/schema",
            "$id": f"https://triad.local/schema/generated/{name}.schema.json",
            "title": name,
        }
        # Deep copy to avoid modifying original
        schema_copy = json.loads(json.dumps(schema))
        convert_nullable(schema_copy)
        schema_doc.update(schema_copy)
        out_path = OUTPUT_DIR / f"{name}.schema.json"
        out_path.write_text(json.dumps(schema_doc, indent=2) + "\n")

    print(f"Exported {len(schemas)} schemas to {OUTPUT_DIR}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
