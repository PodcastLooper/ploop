FROM adoptopenjdk:11-openj9 as build
WORKDIR /scala/app/src
RUN apt-get update && apt-get install -y gnupg2
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update
RUN apt-get install -y sbt
ADD . /scala/app/src
RUN sbt stage
RUN sbt package

FROM adoptopenjdk:11-jre-openj9 as runtime
ENV SERVER_IP=0.0.0.0
ENV SERVER_PORT=8080
WORKDIR /scala/app
COPY --from=build /scala/app/src/target/universal/stage .
RUN ["chmod", "a+x", "bin/ploop-server"]
CMD bin/ploop-server