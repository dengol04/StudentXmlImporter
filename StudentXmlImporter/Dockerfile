FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build

CMD ["java", "-jar", "build/libs/StudentXmlImporter-1.0-SNAPSHOT.jar"]