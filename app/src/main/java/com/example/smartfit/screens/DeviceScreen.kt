package com.example.smartfit.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartfit.ble_device.BLEClient
import com.example.smartfit.components.CustomAlertDialog
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomLargeIconButton
import com.example.smartfit.components.CustomOnlineStateIndicator
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen(
    bleClient: BLEClient? = null,
    onBackClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val bleConnectionState = bleClient?.stateOfDevice?.collectAsStateWithLifecycle()?.value
    val bleListOfDevices = bleClient?.listOfBleDevices?.collectAsStateWithLifecycle()?.value

    when {
        openAlertDialog.value -> {
            CustomAlertDialog(
                onDismissRequest = {
                    bleClient?.stopScan()
                    openAlertDialog.value = false
                },
                dialogTitle = "Najdene vysledky",
                dialogContent = {
                    if (bleListOfDevices?.isNotEmpty() == true) {
                        LazyColumn {
                            items(bleListOfDevices ?: emptyList()) { device ->
                                ListItem(
                                    headlineContent = {
                                        NormalText(device.device.name ?: "Nezname zariadenie")
                                    },
                                    modifier = Modifier.clickable {
                                        bleClient?.connectToDevice(device.device)
                                        //selectedDeviceName.value = device.device.name ?: "Neznámé zařízení"
                                        bleClient?.stopScan()
                                        openAlertDialog.value = false
                                    },
                                    supportingContent = {
                                        NormalText(device.device.address)
                                    },
                                    trailingContent = {
                                        Icon(
                                            imageVector = Icons.Default.Bluetooth,
                                            contentDescription = "Icon of device"
                                        )
                                    },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                                )
                                if (device != bleListOfDevices?.lastOrNull()) {
                                    HorizontalDivider()
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max),
                            contentAlignment = Alignment.Center
                        ) {
                            NormalText("Ziadne najdene zariadenia")
                        }
                    }
                },
                showConfirm = false
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Pridaj zariadenie") },
                navigationIcon = {
                    IconButton(onClick = {
                        bleClient?.stopScan()
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                )
            )
        },
        bottomBar = {
            if (bleConnectionState != 0)
                CustomButton(
                    modifier = Modifier.padding(20.dp),
                    onClick = {
                        bleClient?.disconnect()
                        onDisconnectClick()
                    },
                    buttonText = "Odpojit sa"
                )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bleConnectionState == 0) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CustomLargeIconButton(
                            onClick = {
                                bleClient.startScan { }

                                openAlertDialog.value = true
                            },
                            icon = Icons.Default.AddCircle
                        )
                    }
                } else {
                    CustomLargeIconButton(
                        onClick = {},
                        icon = Icons.Filled.Watch
                    )
                    Spacer(Modifier.padding(8.dp))
                    //HeadlineText(selectedDeviceName.value)
                    Spacer(Modifier.padding(8.dp))
                    CustomOnlineStateIndicator(
                        text = when (bleConnectionState) {
                            0 -> "Offline"
                            1 -> "Pripajanie"
                            2 -> "Pripraveny"
                            else -> "Disconnected"
                        },
                        indicatorColor = when (bleConnectionState) {
                            0 -> MaterialTheme.colorScheme.error
                            1 -> Color.Yellow
                            2 -> Color.Green
                            else -> MaterialTheme.colorScheme.error
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DeviceScreenPreview() {

    DeviceScreen(
        onBackClick = {},
        bleClient = null,
        onDisconnectClick = {}
    )

}