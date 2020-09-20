FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-upms

WORKDIR /hdcloud-upms

EXPOSE 5002

ADD ./hdcloud-upms/hdcloud-upms-biz/target/hdcloud-upms-biz.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-upms-biz.jar
