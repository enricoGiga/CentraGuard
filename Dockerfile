FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp
ADD build/libs/*.jar app.jar
ADD build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]
