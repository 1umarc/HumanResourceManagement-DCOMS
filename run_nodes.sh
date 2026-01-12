JAR="target/your-project-0.1.0-SNAPSHOT.jar"
PKG="nodes"

REGISTRY_PORT=5000
SERVER_PORTS=(6000 6001 6002)
CLIENT_COUNT=3

mvn clean package

echo "Starting registry..."
java -cp "$JAR" "$PKG.RegistryNode" "$REGISTRY_PORT" &
sleep 1   # registry boot is fast and deterministic

echo "Starting servers..."
READY_COUNT=0
TOTAL_SERVERS=${#SERVER_PORTS[@]}

for PORT in "${SERVER_PORTS[@]}"; do
  java -cp "$JAR" "$PKG.ServerNode" "$REGISTRY_PORT" "$PORT" | while read line; do
    echo "[server:$PORT] $line"
    if [[ "$line" == READY* ]]; then
      ((READY_COUNT++))
      if [[ "$READY_COUNT" -eq "$TOTAL_SERVERS" ]]; then
        echo "All servers ready"
      fi
    fi
  done &
done

# Wait until all servers are ready
while [[ "$READY_COUNT" -lt "$TOTAL_SERVERS" ]]; do
  sleep 0.2
done

echo "Starting clients..."
for ((i=0; i<CLIENT_COUNT; i++)); do
  java -cp "$JAR" "$PKG.ClientNode" "$REGISTRY_PORT" &
done

wait
