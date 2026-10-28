#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf out MiniBank.jar
mkdir -p out
find src/main/java -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d out
printf 'Main-Class: MiniBank\n' > manifest.mf
jar cfm MiniBank.jar manifest.mf -C out .
echo "Build successful: MiniBank.jar"
