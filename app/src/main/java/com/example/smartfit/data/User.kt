package com.example.smartfit.data

data class User(
    val id: String = "",
    val displayName: String = "",
    val profilePicUrl: String = "",
    val birthDate: Long = 0L,
    val height: Float = 0f,
    val weight: Float = 0f,
    val bio: String = "",
    val color: Int = 0,
    val isTrainer: Boolean = false,
    val activityGoal: String = "",
    val stepsGoal: String = "",
    val caloriesGoal: String = ""
)