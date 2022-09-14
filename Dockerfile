FROM amazoncorretto:18.0.2
EXPOSE 8080
RUN mkdir -p /app/
#ARG envBarebone = barebone
ADD target/*.jar /app/barebone.jar
#ADD client.truststore.msk.jks /app/client.truststore.msk.jks
#ADD client.truststore.strimzi.jks /app/client.truststore.strimzi.jks
#RUN echo "$envBarebone"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/barebone.jar"]
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar %{envServiceJar}"]