FROM eclipse-temurin:23-jdk-alpine AS base


# Install Docker in Docker (dind)
FROM docker:20.10.24-dind AS dind

# Set environment variables for Docker
ENV DOCKER_CLI_EXPERIMENTAL=enabled
ENV DOCKER_CGROUPS=1


# Copy the base image contents
COPY --from=base / /

# Copy custom entrypoint script to the container
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

WORKDIR /app

# Copy gradle files
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle.kts /app/build.gradle.kts

# Copy the rest of the project files
COPY src /app/src

# Set environment variables for Java
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"


ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

RUN echo "SPRING_PROFILES_ACTIVE=${PROFILE}"

# Set the custom entrypoint script
ENTRYPOINT ["/entrypoint.sh"]