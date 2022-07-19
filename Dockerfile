FROM maven:3-openjdk-18-slim

COPY . /src
WORKDIR /src
RUN mvn clean install -DskipTests

CMD java -jar target/wikisearch-1.0-SNAPSHOT-jar-with-dependencies.jar