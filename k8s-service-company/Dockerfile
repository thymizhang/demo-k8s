# 指定基础镜像，这是分阶段构建的前期阶段
FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.8_10
# 配置参数
ARG JAR_FILE
ARG EXPOSED_PORT
# 将编译构建得到的jar文件复制到镜像空间中
COPY ${JAR_FILE} application.jar
# 指定访问端口
EXPOSE ${EXPOSED_PORT}
# 启动命令
ENTRYPOINT exec java -Dport=${EXPOSED_PORT} $JAVA_OPTS -jar application.jar

# 构建命令
# mvn clean package dockerfile:build

# push镜像到远程仓库
# mvn dockerfile:push

# test