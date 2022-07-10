package com.example.wikisearch

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.temporal.ChronoUnit

data class WikiSearchResponse(
    val query: WikiQuery
)

data class WikiQuery(
    @SerializedName("searchinfo")
    val searchInfo: WikiSearchInfo,
    val search: List<WikiSearchResult>
)

data class WikiSearchInfo(
    @SerializedName("totalhits")
    val totalHits: Int
)

data class WikiSearchResult(
    val title: String,
    val size: Int,
    var language: String = ""
)

fun main() {
    val englishPrefix = "en"
    val japanesePrefix = "ja"
    val searchValue = "絵文字"
    val searchLimit = 100
    val responseFormat = "json"
    val searchProperty = "size"
    val englishWikiSearch =
        "https://$englishPrefix.wikipedia.org/w/api.php?action=query&list=search&srsearch=$searchValue&srlimit=$searchLimit&format=$responseFormat&srprop=$searchProperty&utf8="
    val japaneseWikiSearch =
        "https://$japanesePrefix.wikipedia.org/w/api.php?action=query&list=search&srsearch=$searchValue&srlimit=$searchLimit&format=$responseFormat&srprop=$searchProperty&utf8="

    val client = HttpClient.newBuilder().build()

    val englishRequest = HttpRequest.newBuilder().uri(URI.create(englishWikiSearch))
        .timeout(Duration.of(3, ChronoUnit.SECONDS))
        .GET()
        .build()

    val japaneseRequest = HttpRequest.newBuilder().uri(URI.create(japaneseWikiSearch))
        .timeout(Duration.of(3, ChronoUnit.SECONDS))
        .GET()
        .build()

    val gson = Gson()

    val englishResponse = client.send(englishRequest, HttpResponse.BodyHandlers.ofString()).body()
    val englishSearchResponse: WikiSearchResponse =
        gson.fromJson(englishResponse, WikiSearchResponse::class.java)
    englishSearchResponse.query.search.forEach { it.language = "English" }

    val japaneseResponse = client.send(japaneseRequest, HttpResponse.BodyHandlers.ofString()).body()
    val japaneseSearchResponse: WikiSearchResponse =
        gson.fromJson(japaneseResponse, WikiSearchResponse::class.java)
    japaneseSearchResponse.query.search.forEach { it.language = "Japanese" }

    val output =
        (englishSearchResponse.query.search + japaneseSearchResponse.query.search).sortedByDescending { it.size }
            .subList(0, 20)

    println(gson.toJson(output))
}
