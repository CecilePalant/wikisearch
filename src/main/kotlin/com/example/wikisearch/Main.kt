package com.example.wikisearch

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.temporal.ChronoUnit

// The main method, can be run in IDEA
fun main() {
    // wiki pages in English and japanese are in different domains
    val englishPrefix = "en"
    val japanesePrefix = "ja"

    // the list of query parameter values
    val searchValue = "絵文字" // the string we're looking for
    val searchLimit = 100 // the max number of page per API search call
    val responseFormat = "json" // the data format that the API should return
    val searchProperty = "size" // limit the response data attributes to what we need (title and size)
    // Java URI builder does not accept the pipe | character but title is always included in the response anyway

    // the 2 URLs for the API calls (1 for English, 1 for Japanese)
    // could be refactored but couldn't find a clean way to manage query params using URI or URL in Java...
    val englishWikiSearch =
        "https://$englishPrefix.wikipedia.org/w/api.php?action=query&list=search&srsearch=$searchValue&srlimit=$searchLimit&format=$responseFormat&srprop=$searchProperty&utf8="
    val japaneseWikiSearch =
        "https://$japanesePrefix.wikipedia.org/w/api.php?action=query&list=search&srsearch=$searchValue&srlimit=$searchLimit&format=$responseFormat&srprop=$searchProperty&utf8="

    // the HTTP client
    val client = HttpClient.newBuilder().build()

    // the HTTP request for the English URL
    val englishRequest = HttpRequest.newBuilder().uri(URI.create(englishWikiSearch))
        .timeout(Duration.of(3, ChronoUnit.SECONDS))
        .GET()
        .build()
    // the HTTP request for the Japanese URL
    val japaneseRequest = HttpRequest.newBuilder().uri(URI.create(japaneseWikiSearch))
        .timeout(Duration.of(3, ChronoUnit.SECONDS))
        .GET()
        .build()

    // declared once, used twice
    val gson = Gson()

    // make the first API call to retrieve the pages from the English wiki, deserialize, set language as English for each
    val englishResponse = client.send(englishRequest, HttpResponse.BodyHandlers.ofString()).body()
    val englishSearchResponse: WikiSearchResponse =
        gson.fromJson(englishResponse, WikiSearchResponse::class.java)
    englishSearchResponse.query.search.forEach { it.language = "English" }

    // make the second API call to retrieve the pages from the Japanese wiki, deserialize, set language as Japanese for each
    val japaneseResponse = client.send(japaneseRequest, HttpResponse.BodyHandlers.ofString()).body()
    val japaneseSearchResponse: WikiSearchResponse =
        gson.fromJson(japaneseResponse, WikiSearchResponse::class.java)
    japaneseSearchResponse.query.search.forEach { it.language = "Japanese" }

    // wiki's search API sorting parameter does not support size as an option
    // merge both lists and sort on size, descending
    val output =
        (englishSearchResponse.query.search + japaneseSearchResponse.query.search).sortedByDescending { it.size }
            .subList(0, 20)

    // serialize to json string and print
    println(gson.toJson(output))
}

// data classes define the models
// Not needed attributes and nested objects were removed from the data classes
data class WikiSearchResponse(
    val query: WikiQuery
)

data class WikiQuery(
    val search: List<WikiSearchResult>
)

data class WikiSearchResult(
    val title: String,
    val size: Int,
    var language: String = "" // not set in the API response, set in the main method
)
