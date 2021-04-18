#!/usr/bin/env bash
set -e
set -o pipefail

echo "the current directory is: $(dirname $0)."

cd $(dirname $0)/../..

pwd


./bin/deploy.sh