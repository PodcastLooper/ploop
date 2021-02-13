FROM openjdk:11-alpine as build
WORKDIR /scala/app/src
ADD . /scala/app/src
RUN apk add sbt
RUN sbt package

FROM openjdk:11-alpine
WORKDIR /scala/app
COPY --from=builder /scala/app/src/target/ploop-server.jar /scala/app/ploop-server.jar