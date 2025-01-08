package com.example.smartfit.ble_device

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.smartfit.data.NrfData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

class BLEClient(private val context: Context) {

    companion object {
        const val DEVICE_ADDRESS = "CC:6E:21:A1:D5:0D"
        val SERVICE_UUID: UUID = UUID.fromString("12345678-1234-1234-1234-123456789abc")
        val CHARACTERISTIC_UUID: UUID = UUID.fromString("87654321-4321-4321-4321-210987654321")
    }

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null

    private val _listOfBleDevices = MutableStateFlow<List<ScanResult>>(emptyList())
    val listOfBleDevices = _listOfBleDevices.asStateFlow()

    private val _data = MutableStateFlow(NrfData())
    val data = _data.asStateFlow()

    private val _stateOfDevice = MutableStateFlow(0) //0 - disconnected, 1 - connected, 2 - ready
    val stateOfDevice = _stateOfDevice.asStateFlow()

    private var isReading: Boolean = false

    private var coroutineScope = CoroutineScope(Dispatchers.Default)

    private lateinit var scanCallback: ScanCallback

    private var step_distance: Float = 85.0F
    private var caloriesPerStep: Float = 0.04F


    fun startScan(callback: (BluetoothDevice) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_SCAN not granted")
            return
        }
        scanCallback = object : ScanCallback() {
            @SuppressLint("MissingPermission")
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                if (_listOfBleDevices.value.none { it.device.address == result.device.address }) {
                    _listOfBleDevices.value += result
                }
            }
        }
        bluetoothAdapter?.bluetoothLeScanner?.startScan(scanCallback)
    }

    fun stopScan(
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_SCAN not granted")
            return
        }
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    fun connectToDevice(
        device: BluetoothDevice
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
            return
        }
        Log.e("AHOJBLE", "pred $bluetoothGatt")
        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.i("AHOJBLE", "Connected to GATT server.")
                    Log.e("AHOJBLE", "$bluetoothGatt")
                    _stateOfDevice.value = 1
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        gatt.discoverServices()
                    }
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.i("AHOJBLE", "Disconnected from GATT server.")
                    _stateOfDevice.value = 0
                }
            }

            private val lastUpdateTime = AtomicLong(0)
            private val debouncePeriod = 5000L // 5 sekund
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                super.onServicesDiscovered(gatt, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt?.getService(SERVICE_UUID)
                    if (service != null) {
                        _stateOfDevice.value = 2
                        val characteristic = service.getCharacteristic(CHARACTERISTIC_UUID)

                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            gatt.setCharacteristicNotification(characteristic, true)

                            val descriptor =
                                characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                                    .apply {
                                        value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                    }
                            gatt.writeDescriptor(descriptor)
                        } else {
                            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
                        }
                    } else {
                        _stateOfDevice.value = 1
                        Log.w("AHOJBLE", "Service not found")
                    }
                } else {
                    Log.w("AHOJBLE", "onServicesDiscovered received: $status")
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    Log.i(
//                        "AHOJBLE",
//                        "onRead ${getParsedToObject(characteristic.value?.toString(Charsets.UTF_8) ?: "")}"
//                    )
                    //_data.value = getParsedToObject(characteristic.value?.toString(Charsets.UTF_8) ?: "")
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.i("AHOJBLE", "Characteristic written: ${characteristic.value}")
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                super.onCharacteristicChanged(gatt, characteristic)
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastUpdateTime.get() >= debouncePeriod) {
                    lastUpdateTime.set(currentTime)
//                    coroutineScope.launch {
////                        val parsedData = withContext(Dispatchers.Default) {
////                            getParsedToObject(characteristic?.value?.toString(Charsets.UTF_8) ?: "")
////                            Log.i("AHOJBLE", "onChanged ${getParsedToObject(characteristic?.value?.toString(Charsets.UTF_8) ?: "")}")
////                        }
//                    }
                    Log.d("AHOJBLE", "changed: ${characteristic?.value?.toString(Charsets.UTF_8)}")

                    _data.value =
                        getParsedToObject(characteristic?.value?.toString(Charsets.UTF_8) ?: "")
                }
            }
        })
    }

    fun resetCharacteristic() {
        Log.e("AHOJBLE", "v reset $bluetoothGatt")

        Log.e("AHOJBLE", "VNUTRI RESET")
        val value = "999;999;999;999".toByteArray(Charsets.UTF_8)
        writeCharacteristic(value)
    }

    fun startReading() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
            return
        }
        bluetoothGatt?.let { gatt ->
            val service: BluetoothGattService? = gatt.getService(SERVICE_UUID)
            val characteristic: BluetoothGattCharacteristic? =
                service?.getCharacteristic(CHARACTERISTIC_UUID)
            isReading = true

            coroutineScope.launch {
                while (isReading) {
                    gatt.readCharacteristic(characteristic)
                    delay(30000)
                }
            }
            //gatt.readCharacteristic(characteristic)

        }
    }

    fun writeCharacteristic(value: ByteArray) {
        Log.e("AHOJBLE", "VNUTRI WRITE")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
            return
        }

        val gatt = bluetoothGatt
        if (gatt == null) {
            Log.e("AHOJBLE", "bluetoothGatt is null")
            return
        }

        if (_stateOfDevice.value != 2) {
            Log.e("AHOJBLE", "Device is not ready")
            return
        }

        val service: BluetoothGattService? = gatt.getService(SERVICE_UUID)
        if (service == null) {
            Log.e("AHOJBLE", "Service not found")
            return
        }

        val characteristic: BluetoothGattCharacteristic? =
            service.getCharacteristic(CHARACTERISTIC_UUID)
        if (characteristic == null) {
            Log.e("AHOJBLE", "Characteristic not found")
            return
        }

        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE == 0) {
            Log.e("AHOJBLE", "Characteristic does not support write")
            return
        }

        if (value.isEmpty()) {
            Log.e("AHOJBLE", "Value is empty")
            return
        }

        characteristic.value = value
        val success = gatt.writeCharacteristic(characteristic)
        if (success) {
            Log.i("AHOJBLE", "Characteristic write initiated successfully")
        } else {
            Log.e("AHOJBLE", "Characteristic write failed to initiate")
        }
    }

    fun disconnect() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
            return
        }
        _data.value = NrfData()
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        _stateOfDevice.value = 0
        _listOfBleDevices.value = mutableListOf()
        Log.e("AHOJBLE", "NA KONCI DISCONNECT")
    }

    private fun getParsedToObject(input: String): NrfData {
        val parts = input.split(";")
        return NrfData(
            tep = parts[0],
            teplota = parts[1],
            kroky = parts[2],
            saturacia = parts[3],
            vzdialenost = String.format(
                Locale.getDefault(),
                "%.2f",
                ((step_distance / 100) * parts[2].toFloat())
            ),
            spaleneKalorie = (caloriesPerStep * parts[2].toFloat()).toInt().toString()
        )
    }
}