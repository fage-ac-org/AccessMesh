# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-auth.jar app.jar

# ========== 阶段 2：最小运行时（JRE Alpine，体积远小于 JDK） ==========
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-auth" \
      org.opencontainers.image.description="RuoYi Cloud Auth"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    SERVER_PORT=9210 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/auth/logs /ruoyi/auth/temp /ruoyi/skywalking/agent; \
    addgroup -g 1000 -S app; \
    adduser -u 1000 -S -G app -D -h /ruoyi/auth -s /sbin/nologin app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/auth

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9210

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/auth/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
