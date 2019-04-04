FROM openjdk:8-jre-alpine
EXPOSE 8090
#ENV JAVA_OPTS -Duser.timezone=America/Sao_Paulo
ADD /target/validation-url-1.0.0.jar validation-url-1.0.0.jar
#ENTRYPOINT exec java $JAVA_OPTS -jar /opt/api.jar
CMD java -jar validation-url-1.0.0.jar