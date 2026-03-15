#!/bin/bash

set -e

JAR="target/HRM-1.0.jar"
PKG="nodes"

REGISTRY_PORT=6000
SERVER_COUNT=1
DATABASE_COUNT=1
CLIENT_COUNT=1

PIDS=()

cleanup() {
  echo
  echo "Shutting down cluster..."

  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null || true
  done

  exit 0
}

trap cleanup SIGINT SIGTERM

echo "Building..."
mvn clean package

echo "Starting registry..."
java -cp "$JAR" "$PKG.RegistryNode" "$REGISTRY_PORT" &
PIDS+=($!)
sleep 1

echo "Starting Database Servers..."
for ((i=0; i<DATABASE_COUNT; i++)); do
  java -cp "$JAR" "$PKG.DatabaseNode" "$REGISTRY_PORT" &
  PIDS+=($!)
done

sleep 5

echo "Starting servers..."
for ((i=0; i<SERVER_COUNT; i++)); do
  java -cp "$JAR" "$PKG.ServerNode" "$REGISTRY_PORT" &
  PIDS+=($!)
done

sleep 5

echo "Starting clients..."
for ((i=0; i<CLIENT_COUNT; i++)); do
  java -cp "$JAR" "$PKG.ClientNode" "$REGISTRY_PORT"
  PIDS+=($!)
done

wait
