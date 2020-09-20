FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-mp-manager

WORKDIR /hdcloud-mp-manager

EXPOSE 6000

ADD ./hdcloud-visual/hdcloud-mp-manager/target/hdcloud-mp-manager.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-mp-manager.jar
