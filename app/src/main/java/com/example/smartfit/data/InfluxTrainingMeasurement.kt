package com.example.smartfit.data

import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement
import java.time.Instant

@Measurement(name = "training")
data class InfluxTrainingMeasurement(
    @Column(tag = true) val userId: String,
    @Column(tag = true) val trainingId: String,
    @Column val tep: Int,
    @Column val teplota: Double,
    @Column val kroky: Int,
    @Column val kadencia: Int,
    @Column val spaleneKalorie: Int,
    @Column val vzdialenost: Double,
    @Column val saturacia: Double,
    @Column val rychlost: Int,
    @Column(timestamp = true) val time: Instant
)
