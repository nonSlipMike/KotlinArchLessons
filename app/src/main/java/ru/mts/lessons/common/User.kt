package ru.mts.lessons.common

data class User (
    var id: Long = 0,
    var name: String? = null,
    var email: String? = null,
)