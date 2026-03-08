# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-workflow.jar app.jar

# ========== 阶段 2：最小运行时（JRE Alpine，体积远小于 JDK） ==========
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-workflow" \
      org.opencontainers.image.description="RuoYi Cloud Workflow Module"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    SERVER_PORT=9205 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/workflow/logs /ruoyi/workflow/temp /ruoyi/skywalking/agent; \
    addgroup -g 1000 -S app; \
    adduser -u 1000 -S -G app -D -h /ruoyi/workflow -s /sbin/nologin app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/workflow

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9205

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/workflow/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
