FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /hdcloud-auth

WORKDIR /hdcloud-auth

EXPOSE 5001

ADD ./hdcloud-auth/target/hdcloud-auth.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar hdcloud-auth.jar
