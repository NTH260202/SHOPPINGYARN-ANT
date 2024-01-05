FROM bellsoft/liberica-runtime-container:jdk-17-slim-musl AS layers
ARG SOURCE_DIR
WORKDIR /home/app
COPY ./${SOURCE_DIR}/target/*.jar ./application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM bellsoft/liberica-openjre-alpine-musl:17-aarch64
WORKDIR /home/app
COPY --from=layers /home/app/dependencies/ ./
COPY --from=layers /home/app/spring-boot-loader/ ./
COPY --from=layers /home/app/snapshot-dependencies/ ./
COPY --from=layers /home/app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080