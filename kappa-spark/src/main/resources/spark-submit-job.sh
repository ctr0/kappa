#!/usr/bin/env bash

./bin/spark-submit \
  --class {kappa.spark.$job.class} \
  --master {kappa.spark.master} \
  --deploy-mode {kappa.spark.deploy-mode} \
  --supervise \
  --executor-memory {kappa.spark.$job.executor-memory} \
  --total-executor-cores {kappa.spark.$job.total-executor-cores} \
  {kappa.spark.$job.resource} {kappa.spark.$job.arguments}