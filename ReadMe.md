Task Instructions
=========

Wikipedia has a [free open search API](https://www.mediawiki.org/wiki/API:Search).

Write a simple program that queries for 100 pages containing the terms `絵文字` in both English and Japanese pages and out
of the combined results, output the page title, page size and page language ranked by page size. Only output the first
20 entries. Can be done in any programming language you want.

How to run
=========
This project uses Kotlin (with Java 18) and maven. The only runtime dependency is Google Gson for
serialization/deserialization.

With IntelliJ: open project and run `fun main()` from `com/example/wikisearch/Main.kt`

Using maven, generate a 'fat-jar', in a terminal run `mvn clean install`
go where `wikisearch-1.0-SNAPSHOT-jar-with-dependencies.jar` is located,
using Java, run ` java -jar wikisearch-1.0-SNAPSHOT-jar-with-dependencies.jar`

About
=========

Since this program is pretty straight forward, it is contained in one file only and does not have any unit tests.
If it were part of a bigger project, it would have 1 or several classes for the models,
1 service class that would handle the 'business logic', one class to manage the HTTP client, request, response, which
would be called by the service class.
The unit tests would cover the service methods (indirectly covering the instantiation of the models).
The HTTP layer and serialization/deserialization layer wouldn't need unit tests.

Error management is also missing from this program: if the connection is interrupted or if the server returns something
other than a 200, then the program would fail.


Misc.
=========
`src/main/resources/com/example/wikisearch/wiki.http` can also be used in IDEA to run the 2 API requests, but it does
not handle the 'business logic' of tagging the language, merging, sorting and outputting the response.
