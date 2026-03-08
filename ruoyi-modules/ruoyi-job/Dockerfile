# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-job.jar app.jar

# ========== 阶段 2：Zulu OpenJDK 17 JRE（Ubuntu 基础，glibc + libstdc++） ==========
FROM azul/zulu-openjdk:17-jre

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-job" \
      org.opencontainers.image.description="RuoYi Cloud Job Module (Snail Job)"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    SERVER_PORT=9203 \
    SNAIL_PORT=29203 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/job/logs /ruoyi/job/temp /ruoyi/skywalking/agent; \
    groupadd -g 1000 app; \
    useradd -u 1000 -g app -d /ruoyi/job -s /sbin/nologin -M app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/job

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9203 29203

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -Dsnail-job.port=${SNAIL_PORT} \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/job/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
