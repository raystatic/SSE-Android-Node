package com.example.ssedemo

data class SSEEventData(
    val status: STATUS? = null,
    val image: String? = null
)

enum class STATUS {
    SUCCESS,
    ERROR,
    NONE,
    CLOSED,
    OPEN
}