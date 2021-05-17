FROM java:11
MAINTAINER pcdd <1907263405@qq.com>
ADD blogApi-1.0.jar blogApi.jar
CMD java -jar blogApi.jar
ENV LANG en_US.UTF-8
# 设置容器时区为东八区，不写这句部署后的时间和本地时间相差8小时，会引发一些bug
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
