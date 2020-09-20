FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-daemon-elastic-job

WORKDIR /hdcloud-daemon-elastic-job

EXPOSE 5002

ADD ./hdcloud-visual/hdcloud-daemon-elastic-job/target/hdcloud-daemon-elastic-job.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-daemon-elastic-job.jar
