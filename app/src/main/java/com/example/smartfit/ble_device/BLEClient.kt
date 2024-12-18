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
import kotlinx.coroutines.withContext
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
        //    scanCallback: ScanCallback
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
        //                    , callback: (Boolean) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("AHOJBLE", "Permission BLUETOOTH_CONNECT not granted")
            return
        }
        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.i("AHOJBLE", "Connected to GATT server.")
                    //              callback(true)
                    _stateOfDevice.value = 1
                    //isConnected = true
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
                    //            callback(false)
                }
            }

            private val lastUpdateTime = AtomicLong(0)
            private val debouncePeriod = 5000L // 5 sekund
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    //_isReady.value = true
//                    _stateOfDevice.value = 2
//                    Log.i("AHOJBLE", "Services discovered.")
//                } else {
//                    //_isReady.value = false
//                    _stateOfDevice.value = 1
//                    Log.w("AHOJBLE", "onServicesDiscovered received: $status")
//                }
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
                    coroutineScope.launch {
                        val parsedData = withContext(Dispatchers.Default) {
                            getParsedToObject(characteristic?.value?.toString(Charsets.UTF_8) ?: "")
                            Log.i(
                                "AHOJBLE",
                                "onChanged ${
                                    getParsedToObject(
                                        characteristic?.value?.toString(
                                            Charsets.UTF_8
                                        ) ?: ""
                                    )
                                }"
                            )
                        }
                        //_data.value = parsedData
                    }
                }
            }
        })
    }

//    fun getListOfScannedDevices(): MutableList<ScanResult> {
//        return listOfBleDevices
//    }

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

    fun stopReading() {
        isReading = false
    }

    fun writeCharacteristic(value: ByteArray) {
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
            characteristic?.value = value
            gatt.writeCharacteristic(characteristic)
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
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        //_isReady.value = false
        _stateOfDevice.value = 0
        _listOfBleDevices.value = mutableListOf()
    }

    private fun getParsedToObject(input: String): NrfData {
        val parts = input.split(";")
        return NrfData(
            tep = parts[0],
            teplota = parts[1],
            steps = parts[2],
            saturacia = parts[3]
        )
    }
}