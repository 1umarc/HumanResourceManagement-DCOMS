#!/bin/bash

set -e

JAR="target/your-project-0.1.0-SNAPSHOT.jar"
PKG="nodes"

REGISTRY_PORT=5000
SERVER_PORTS=(6000 6001 6002)
DATABASE_SERVER_PORTS=(7000)
CLIENT_COUNT=3

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
for PORT in "${DATABASE_SERVER_PORTS[@]}"; do
  java -cp "$JAR" "$PKG.DatabaseNode" "$REGISTRY_PORT" "$PORT" &
  PIDS+=($!)
done

echo "Starting servers..."
for PORT in "${SERVER_PORTS[@]}"; do
  java -cp "$JAR" "$PKG.ServerNode" "$REGISTRY_PORT" "$PORT" &
  PIDS+=($!)
done

echo "Starting clients..."
for ((i=0; i<CLIENT_COUNT; i++)); do
  java -cp "$JAR" "$PKG.ClientNode" "$REGISTRY_PORT" &
  PIDS+=($!)
done

wait
