#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
NAME="owasp10-spring-demo"
OUT="bundle"
rm -rf "$OUT"
mkdir -p "$OUT/$NAME"
# include compiled jar and runtime deps if present
mkdir -p "$OUT/$NAME/target/dependency"
if [ -d "target/dependency" ]; then cp -r target/dependency "$OUT/$NAME/target/"; fi
if ls target/*.jar >/dev/null 2>&1; then cp target/*.jar "$OUT/$NAME/target/"; fi
# include sources
rsync -a --exclude 'bundle' --exclude '.git' --exclude '.idea' --exclude 'target' ./ "$OUT/$NAME/"
cd "$OUT"
zip -qr "${NAME}-with-deps.zip" "$NAME"
echo "Created $OUT/${NAME}-with-deps.zip"
