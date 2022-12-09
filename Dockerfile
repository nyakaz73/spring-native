# FROM paketobuildpacks/builder:tiny
# WORKDIR /work/
# # RUN chown 1001 /work \
# #     && chmod "g+rwX" /work \
# #     && chown 1001:root /work
#
# COPY  target/spring-native /work/application
#
# # ENV TZ="Africa/Johannesburg"
#
# EXPOSE 8080
#
# USER 1001
#
# ENTRYPOINT ["/application"]


# FROM paketobuildpacks/builder:tiny
# ARG APP_FILE
# EXPOSE 8080
# COPY target/${APP_FILE} app
# ENTRYPOINT ["/jibber"]

#FROM --platform=linux/amd64 container-registry.oracle.com/os/oraclelinux:8-slim
#FROM --platform=linux/amd64 paketobuildpacks/builder:tiny
FROM --platform=linux/amd64 gcr.io/distroless/base
USER root
ARG PORT=8080
EXPOSE ${PORT}
RUN pwd
RUN mkdir /work
WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work

RUN cd /work

COPY target/spring-native /work/app   # Copy the native executable into the root directory and call it "app"
ARG APP_FILE=target/spring-native                 ## Pass in the native executable
ENTRYPOINT ["/work/app"]          # Just run the native executable :)


# docker build -f Dockerfile --build-arg APP_FILE=./target/spring-native -t spring/mynative .
