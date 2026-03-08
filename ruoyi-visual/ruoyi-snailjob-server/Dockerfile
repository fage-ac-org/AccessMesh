# ========== 阶段 1：仅携带构建产物（CI 已用 Maven 打好 jar） ==========
FROM alpine:3.19 AS builder
WORKDIR /build
COPY target/ruoyi-snailjob-server.jar app.jar

# ========== 阶段 2：最小运行时（JRE Alpine，体积远小于 JDK） ==========
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Lion Li"
LABEL org.opencontainers.image.title="ruoyi-snailjob-server" \
      org.opencontainers.image.description="RuoYi Snail Job Server"

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_OPTS=""

RUN set -eux; \
    mkdir -p /ruoyi/snailjob/logs; \
    addgroup -g 1000 -S app; \
    adduser -u 1000 -S -G app -D -h /ruoyi/snailjob -s /sbin/nologin app; \
    chown -R app:app /ruoyi

WORKDIR /ruoyi/snailjob

COPY --from=builder /build/app.jar ./app.jar
RUN chown app:app app.jar

USER app

EXPOSE 8800 17888

ENTRYPOINT ["sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/ruoyi/snailjob/logs \
  -XX:+UseZGC \
  ${JAVA_OPTS} \
  -jar app.jar"]
