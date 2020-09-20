FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-pay

WORKDIR /hdcloud-pay

EXPOSE 5003

ADD ./hdcloud-visual/hdcloud-pay/target/hdcloud-pay.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-pay.jar
