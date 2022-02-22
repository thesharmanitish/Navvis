FROM fedora:latest
MAINTAINER Nitish Sharma<nisharma@redhat.com>
USER root
RUN yum update --security --sec-severity=Critical --skip-broken  --nobest -y  &&  yum clean all \
&& yum install -y java
RUN update-ca-trust extract
EXPOSE 8080
EXPOSE 443
EXPOSE 8443
RUN mkdir -p /build && chmod 777 /build
WORKDIR /build
COPY target/CodingTest-0.0.1-SNAPSHOT.jar /build/

ENV JAVA_OPTS -Xmx1024m
#CMD export COMMIT_HASH=$(cat /build/commit_hash);
CMD exec java $JAVA_OPTS -jar CodingTest-0.0.1-SNAPSHOT.jar