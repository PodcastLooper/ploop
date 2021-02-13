FROM adoptopenjdk:11-openj9 as build
WORKDIR /scala/app/src
RUN apt-get update
RUN apt-get install -y sbt
ADD . /scala/app/src
RUN sbt stage
RUN sbt package

FROM adoptopenjdk:11-jre-openj9 as runtime
WORKDIR /scala/app
COPY --from=build /scala/app/target/universal/stage .
RUN ["chmod", "a+x", "bin/ploop-server.sh"]
CMD bin/ploop-server.sh