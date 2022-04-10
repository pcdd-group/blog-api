FROM openjdk:11.0.14.1-slim
ARG JAR_FILE=target/blog-api-1.0.jar
WORKDIR /blogapi
RUN mkdir -p /blog-api
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ \
"java", "-Xms64m", "-Xmx64m", "-Dfile.encoding=UTF-8", \
"-Duser.timezone=GMT+08", \
"-Dspring.profiles.include=prod,private", \
"-Djava.security.egd=file:/dev/./urandom", \
"-jar", "app.jar"]
EXPOSE 9333
