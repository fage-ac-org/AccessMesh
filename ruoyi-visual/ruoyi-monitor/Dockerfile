# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-monitor.jar app.jar

# ========== 阶段 2：最小运行时（JRE Alpine，体积远小于 JDK） ==========
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-monitor" \
      org.opencontainers.image.description="RuoYi Cloud Monitor (Spring Boot Admin)"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/monitor/logs; \
    addgroup -g 1000 -S app; \
    adduser -u 1000 -S -G app -D -h /ruoyi/monitor -s /sbin/nologin app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/monitor

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9100

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/monitor/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
