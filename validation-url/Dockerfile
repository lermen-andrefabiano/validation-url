FROM openjdk:8-jre-alpine

EXPOSE 8080

RUN echo $JAVA_HOME $PATH

RUN java -version

CMD ["java","-version"]
	
ADD /target/validation-url-1.0.0.jar validation-url.jar

ENTRYPOINT ["java", "-jar", "validation-url.jar"]