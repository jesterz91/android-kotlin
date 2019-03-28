package io.github.jesterz91.dustinfo.model

// API 요청 결과 모델 클래스

data class FineDust(
    val weather: Weather,
    val common: Common,
    val result: Result
)

data class Weather(
    val dust: List<Dust>
)

data class Dust(
    val station: Station,
    val timeObservation: String,
    val pm10: Pm10
)

data class Station(
    val longitude: String,
    val latitude: String,
    val name: String,
    val id: String
)

data class Pm10(
    val grade: String,
    val value: String
)

data class Common(
    val alertYn: String,
    val stormYn: String
)

data class Result(
    val code: Int,
    val requestUrl: String,
    val message: String
)