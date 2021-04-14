#!/usr/bin/env bash




set -e
set -o pipefail

echo "going to deploy..."
export PROJECT_ID=${GCLOUD_PROJECT}

APP_NAME=spring-native-kubernetes

kubectl get pods