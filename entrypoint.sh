#!/bin/sh

# Start Docker daemon in the background
dockerd-entrypoint.sh &

# Wait for Docker daemon to start
while(! docker info > /dev/null 2>&1); do
    echo "Waiting for Docker to start..."
    sleep 1
done

echo "Docker is up and running!"
# Verify installation
java -version

echo "Java installed successfully!"

cd /app && ./gradlew build --no-daemon
cp /app/build/libs/*.jar ./app.jar
java -jar ./app.jar --server.port=80