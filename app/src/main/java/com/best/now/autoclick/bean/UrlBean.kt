package com.best.now.autoclick.bean

data class UrlBean(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
)

data class Data(
    val id: Int,
    val url: String
)