# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-resource.jar app.jar

# ========== 阶段 2：Zulu OpenJDK 17 JRE（Ubuntu 基础，glibc + libstdc++） ==========
FROM azul/zulu-openjdk:17-jre

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-resource" \
      org.opencontainers.image.description="RuoYi Cloud Resource Module"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    SERVER_PORT=9204 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/resource/logs /ruoyi/resource/temp /ruoyi/skywalking/agent; \
    groupadd -g 1000 app; \
    useradd -u 1000 -g app -d /ruoyi/resource -s /sbin/nologin -M app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/resource

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9204

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/resource/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
