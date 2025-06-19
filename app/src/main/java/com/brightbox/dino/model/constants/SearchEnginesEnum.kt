package com.brightbox.dino.model.constants

enum class SearchEnginesEnum(val engineName: String, val baseUrl: String) {
    GOOGLE(
        engineName = "Google",
        baseUrl = "https://www.google.com/search?q="
    ),
    BING(
        engineName = "Bing",
        baseUrl = "https://www.bing.com/search?q="
    ),
    DUCKDUCKGO(
        engineName = "DuckDuckGo",
        baseUrl = "https://duckduckgo.com/?q="
    )
}