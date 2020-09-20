FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-register

WORKDIR /hdcloud-register

EXPOSE 8848

ADD ./hdcloud-register/target/hdcloud-register.jar ./


CMD sleep 60;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-register.jar


