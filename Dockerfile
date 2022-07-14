FROM maven:3-openjdk-18-slim

RUN mkdir /wikisearch

COPY target/wikisearch-1.0-SNAPSHOT-jar-with-dependencies.jar /wikisearch

WORKDIR /wikisearch

CMD java -jar wikisearch-1.0-SNAPSHOT-jar-with-dependencies.jar