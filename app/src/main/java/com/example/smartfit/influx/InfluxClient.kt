package com.example.smartfit.influx

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.smartfit.BuildConfig
import com.example.smartfit.data.InfluxData
import com.example.smartfit.data.InfluxTrainingMeasurement
import com.example.smartfit.data.NrfData
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.Locale

class InfluxClient {
    private val influxDBClientKotlin: InfluxDBClientKotlin = InfluxDBClientKotlinFactory.create(
        BuildConfig.INFLUX_URL,
        BuildConfig.INFLUX_TOKEN.toCharArray(),
        BuildConfig.INFLUX_ORG,
        BuildConfig.INFLUX_BUCKET
    )

    fun getInstance(): InfluxDBClientKotlin {
        return influxDBClientKotlin
    }

    suspend fun fetchMostRecentInfluxData(userId: String, trainingId: String): NrfData {
        var nrfData = NrfData()

        withContext(Dispatchers.IO) {
            val queryApi = influxDBClientKotlin.getQueryKotlinApi()

            val fluxQuery = """from(bucket: "${BuildConfig.INFLUX_BUCKET}")
            |> range(start: -1h)
            |> filter(fn: (r) => r["_measurement"] == "training")
            |> filter(fn: (r) => r["userId"] == "$userId")
            |> filter(fn: (r) => r["trainingId"] == "$trainingId")
            |> last()"""

            queryApi.query(fluxQuery).consumeAsFlow().collect { record ->
                when (record.values["_field"].toString()) {
                    "tep" -> nrfData = nrfData.copy(tep = record.values["_value"].toString())
                    "teplota" -> nrfData =
                        nrfData.copy(teplota = record.values["_value"].toString())

                    "rychlost" -> nrfData =
                        nrfData.copy(rychlost = record.values["_value"].toString())

                    "kadencia" -> nrfData =
                        nrfData.copy(kadencia = record.values["_value"].toString())

                    "saturacia" -> nrfData =
                        nrfData.copy(saturacia = record.values["_value"].toString())

                    "vzdialenost" -> nrfData =
                        nrfData.copy(vzdialenost = record.values["_value"].toString())

                    "spaleneKalorie" -> nrfData =
                        nrfData.copy(spaleneKalorie = record.values["_value"].toString())

                    "kroky" -> nrfData = nrfData.copy(kroky = record.values["_value"].toString())
                }
            }
        }
        return nrfData
    }

    suspend fun fetchInfluxData(userId: String, trainingId: String): InfluxData {

        var influxData = InfluxData()

        withContext(Dispatchers.IO) {
            influxDBClientKotlin.use {
                val queryApi = influxDBClientKotlin.getQueryKotlinApi()

                val minValuesDeferred = async {
                    val minValues = mutableMapOf<String, Any>()
                    val fluxQueryMin = """from(bucket: "${BuildConfig.INFLUX_BUCKET}")
                |> range(start: 0)
                |> filter(fn: (r) => r["_measurement"] == "training")
                |> filter(fn: (r) => r["userId"] == "$userId")
                |> filter(fn: (r) => r["trainingId"] == "$trainingId")
                |> filter(fn: (r) => r["_field"] == "tep" or r["_field"] == "teplota")
                |> group(columns: ["_field"])
                |> min()"""
                    queryApi.query(fluxQueryMin).consumeAsFlow().collect { record ->
                        minValues[record.values["_field"].toString()] = record.values["_value"]!!
                    }
                    minValues
                }

                val maxValuesDeferred = async {
                    val maxValues = mutableMapOf<String, Any>()
                    val fluxQueryMax = """from(bucket: "${BuildConfig.INFLUX_BUCKET}")
                |> range(start: 0)
                |> filter(fn: (r) => r["_measurement"] == "training")
                |> filter(fn: (r) => r["userId"] == "$userId")
                |> filter(fn: (r) => r["trainingId"] == "$trainingId")
                |> filter(fn: (r) => r["_field"] == "tep" or r["_field"] == "teplota")
                |> group(columns: ["_field"])
                |> max()"""
                    queryApi.query(fluxQueryMax).consumeAsFlow().collect { record ->
                        maxValues[record.values["_field"].toString()] = record.values["_value"]!!
                    }
                    maxValues
                }

                val avgValuesDeferred = async {
                    val fluxQueryAvg = """from(bucket: "${BuildConfig.INFLUX_BUCKET}")
                |> range(start: 0)
                |> filter(fn: (r) => r["_measurement"] == "training")
                |> filter(fn: (r) => r["userId"] == "$userId")
                |> filter(fn: (r) => r["trainingId"] == "$trainingId")
                |> filter(fn: (r) => r["_field"] == "rychlost" or r["_field"] == "kadencia" or r["_field"] == "saturacia" or r["_field"] == "teplota")
                |> mean()"""
                    queryApi.query(fluxQueryAvg).consumeAsFlow().collect { record ->
                        when (record.values["_field"].toString()) {
                            "rychlost" -> influxData = influxData.copy(
                                avgRychlost = record.values["_value"].toString().toFloat().toInt()
                                    .toString()
                            )

                            "kadencia" -> influxData = influxData.copy(
                                avgKadencia = record.values["_value"].toString().toFloat().toInt()
                                    .toString()
                            )

                            "saturacia" -> influxData =
                                influxData.copy(avgSaturacia = record.values["_value"].toString())

                            "teplota" -> influxData = influxData.copy(
                                teplota = String.format(
                                    Locale.US,
                                    "%.1f",
                                    record.values["_value"].toString().toFloat()
                                )
                            )

                        }
                    }
                }

                val lastValuesDeferred = async {
                    val fluxQueryLast = """from(bucket: "${BuildConfig.INFLUX_BUCKET}")
                |> range(start: 0)
                |> filter(fn: (r) => r["_measurement"] == "training")
                |> filter(fn: (r) => r["userId"] == "$userId")
                |> filter(fn: (r) => r["trainingId"] == "$trainingId")
                |> filter(fn: (r) => r["_field"] == "vzdialenost" or r["_field"] == "spaleneKalorie" or r["_field"] == "kroky")
                |> last()"""
                    queryApi.query(fluxQueryLast).consumeAsFlow().collect { record ->
                        when (record.values["_field"].toString()) {
                            "vzdialenost" -> influxData =
                                influxData.copy(vzdialenost = record.values["_value"].toString())

                            "spaleneKalorie" -> influxData =
                                influxData.copy(spaleneKalorie = record.values["_value"].toString())

                            "kroky" -> influxData =
                                influxData.copy(kroky = record.values["_value"].toString())
                        }
                    }
                }

                val minValues = minValuesDeferred.await()
                val maxValues = maxValuesDeferred.await()
                avgValuesDeferred.await()
                lastValuesDeferred.await()

                minValues.forEach { (field, minValue) ->
                    when (field) {
                        "tep" -> influxData = influxData.copy(tepMin = minValue.toString())
                    }
                }

                maxValues.forEach { (field, maxValue) ->
                    when (field) {
                        "tep" -> influxData = influxData.copy(tepMax = maxValue.toString())
                    }
                }
            }
        }
        return influxData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadDataToInflux(userId: String, trainingId: String, nrfData: NrfData) {

        withContext(Dispatchers.IO) {
            influxDBClientKotlin.use {
                Log.d("AHOJ", " data ${nrfData.toString()}")
                val writeApi = influxDBClientKotlin.getWriteKotlinApi()
                val mem = InfluxTrainingMeasurement(
                    userId = userId,
                    trainingId = trainingId,
                    tep = nrfData.tep.toInt(),
                    teplota = nrfData.teplota.replace(",", ".").toDouble(),
                    kroky = nrfData.kroky.toInt(),
                    kadencia = nrfData.kadencia.toInt(),
                    spaleneKalorie = nrfData.spaleneKalorie.toInt(),
                    vzdialenost = nrfData.vzdialenost.replace(",", ".").toInt(),
                    saturacia = nrfData.saturacia.replace(",", ".").toDouble(),
                    rychlost = nrfData.rychlost.toInt(),
                    time = Instant.now()
                )
                writeApi.writeMeasurement(mem, WritePrecision.NS)

                Log.d("AHOJ", "influx write ${mem.toString()}")
            }
        }

    }

    fun closeConnection() {
        influxDBClientKotlin.close()
    }
}