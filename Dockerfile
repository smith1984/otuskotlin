#Build stage
FROM gradle:8.1.1-jdk17-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle clean
RUN gradle assemble
RUN tar -xvf ./m3-hw4-vafs-app-ktor/build/distributions/m3-hw4-vafs-app-ktor-0.0.1.tar


#Final stage
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /home/gradle/src/m3-hw4-vafs-app-ktor-0.0.1 /app/

ENTRYPOINT ["./bin/m3-hw4-vafs-app-ktor"]