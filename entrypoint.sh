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

cd /app && ./gradlew clean build --no-daemon
pwd
ls -la
ls -la build
cp build/libs/*.jar ./app.jar
java -jar ./app.jar --server.port=80