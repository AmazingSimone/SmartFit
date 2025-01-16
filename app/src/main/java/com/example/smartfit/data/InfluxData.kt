package com.example.smartfit.data

data class InfluxData(
    val tepMin: String = "0",
    val tepMax: String = "0",
    val teplota: String = "0.0",
    val kroky: String = "0",
    val avgKadencia: String = "0",
    val spaleneKalorie: String = "0",
    val vzdialenost: String = "0",
    val avgSaturacia: String = "0.0",
    val avgRychlost: String = "0"
)
