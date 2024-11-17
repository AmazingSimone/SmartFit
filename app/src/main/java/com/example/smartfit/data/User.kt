package com.example.smartfit.data

import java.time.LocalDateTime

data class User(
    val id: String? = "",
    val displayName: String? = "",
    val profilePicUrl: String? = null,
    val birthDate: Long? = 0L,
    val height: Float? = 0f,
    val weight: Float? = 0f,
    val bio: String? = "",
    val isTrainer: Boolean = false
)