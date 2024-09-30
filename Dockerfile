FROM eclipse-temurin:23-jdk-alpine AS base
LABEL authors="Jose Blas Camacho Taboada"

ADD ./build/libs/inditex*.jar price-api.jar

EXPOSE 80

ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

RUN echo "SPRING_PROFILES_ACTIVE=${PROFILE}"


CMD ["java", "-jar", "price-api.jar", "--server.port=80"]
