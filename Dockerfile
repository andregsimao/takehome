FROM gradle:7.5.1-jdk17-alpine as dependencies-builder
ENV APP_DIR /app
WORKDIR $APP_DIR

RUN mkdir /tmp/cache

COPY build.gradle $APP_DIR/
COPY settings.gradle $APP_DIR/
COPY gradle.properties $APP_DIR/
RUN gradle dependencies -g /tmp/cache

# -----------------------------------------------------------------------------
FROM gradle:7.5.1-jdk17-alpine as builder
ENV APP_DIR /app
WORKDIR $APP_DIR
RUN mkdir /tmp/cache
COPY --from=dependencies-builder /tmp/cache /tmp/cache
COPY . $APP_DIR
RUN gradle assemble -g /tmp/cache --no-daemon

# -----------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /deployments

COPY --from=builder /app/build/libs/*.jar /deployments/

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar takehome-2.2.0.jar"]