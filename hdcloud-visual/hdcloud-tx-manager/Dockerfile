FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-tx-manager

WORKDIR /hdcloud-tx-manager

EXPOSE 5004
EXPOSE 9998

ADD ./hdcloud-visual/hdcloud-tx-manager/target/hdcloud-tx-manager.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-tx-manager.jar
