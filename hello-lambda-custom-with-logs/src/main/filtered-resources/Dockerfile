FROM amazonlinux:2 as base

FROM base as build
ENV LANG=en_US.UTF-8
RUN yum update -y && yum install -y gcc gcc-c++ zlib-devel zip tar gzip && yum clean all
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.3.0/graalvm-ce-java11-linux-amd64-20.3.0.tar.gz -o /tmp/graalvm.tar.gz \
    && tar -zxf /tmp/graalvm.tar.gz -C /tmp \
    && mv /tmp/graalvm-ce-java11-20.3.0 /usr/lib/graalvm \
    && rm -rf /tmp/*
RUN /usr/lib/graalvm/bin/gu install native-image
ENV PATH=/usr/lib/graalvm/bin:${PATH}
ENV JAVA_HOME=/usr/lib/graalvm
RUN yum install -y maven

WORKDIR /home/app
# compile the function
COPY pom.xml .
COPY src src
COPY bootstrap .
RUN mvn clean package -P !lambda-deployment-package
RUN native-image -jar target/${project.artifactId}-${project.version}.jar -H:Name=func --no-fallback

# (Optional) Add Lambda Runtime Interface Emulator and use a script in the ENTRYPOINT for simpler local runs
ADD https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie .
RUN yum install -y zip && yum clean all
RUN chmod 755 aws-lambda-rie
RUN chmod 755 bootstrap
RUN chmod 755 func
RUN zip -j function.zip bootstrap func

FROM base
WORKDIR /function
COPY --from=build /home/app/func /function/func
COPY --from=build /home/app/bootstrap /function/bootstrap
COPY --from=build /home/app/function.zip /function.zip
COPY --from=build /home/app/aws-lambda-rie /usr/bin/aws-lambda-rie
ENTRYPOINT [ "/function/bootstrap" ]
CMD [ "example.App::sayHello" ]
