#!/usr/bin/env bash
set -e
set -o pipefail
export PROJECT_ID=${GCLOUD_PROJECT}
echo "going to deploy..."
mvn verify package deploy
APP_NAME=spring-native-kubernetes
kubectl get pods