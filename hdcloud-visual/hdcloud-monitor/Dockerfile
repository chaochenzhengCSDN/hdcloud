FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-monitor

WORKDIR /hdcloud-monitor

EXPOSE 5001

ADD ./hdcloud-visual/hdcloud-monitor/target/hdcloud-monitor.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-monitor.jar
