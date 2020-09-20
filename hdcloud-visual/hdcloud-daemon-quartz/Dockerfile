FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-daemon-quartz

WORKDIR /hdcloud-daemon-quartz

EXPOSE 5007

ADD ./hdcloud-visual/hdcloud-daemon-quartz/target/hdcloud-daemon-quartz.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-daemon-quartz.jar
