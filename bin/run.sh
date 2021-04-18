#!/usr/bin/env bash

docker run \
  --volume ${HOME}/.kube:/home/cnb/.kube \
  docker.io/library/kubernetes-controller:0.0.1-SNAPSHOT
