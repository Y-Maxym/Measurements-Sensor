FROM maven:latest AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

CMD mvn spring-boot:run