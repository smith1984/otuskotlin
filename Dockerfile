#Build stage
FROM gradle:7.6-jdk17-focal AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

#Final stage
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /home/gradle/src/m1-hw1/build/libs/ /app/

ENTRYPOINT ["java", "-jar", "m1-hw1-0.0.1.jar"]
