FROM openjdk:8-alpine

COPY target/uberjar/businesspartners.jar /businesspartners/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/businesspartners/app.jar"]
