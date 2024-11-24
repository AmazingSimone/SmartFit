package com.example.smartfit.screens

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.Heading1
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTrainingLobby(
    groupTrainingId: String,
    onDeleteClick: () -> Unit
) {

    val context = LocalContext.current
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(groupTrainingId) {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(groupTrainingId, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qrBitmap = bitmap
    }

    qrBitmap?.let { bitmap ->

        val fullscreenQrState = remember { mutableStateOf(true) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Heading1("Skupinovy trening")
                    },
                    actions = {
                        Row {
                            if (!fullscreenQrState.value) {
                                IconButton(onClick = { fullscreenQrState.value = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.QrCode,
                                        contentDescription = "Show qr code icon"
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteClick() }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete training",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
            }

        ) { innerPadding ->


            val padding: Dp = 8.dp
            Surface(modifier = Modifier.padding(innerPadding)) {

                if (fullscreenQrState.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                                IconButton(
                                    onClick = {
                                        fullscreenQrState.value = false
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        "Close qr code fullscreen icon",
                                        tint = MaterialTheme.colorScheme.surface
                                    )
                                }
                            }

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {


                }
            }
        }
    }
}

@Composable
@Preview
fun GroupTrainingLobbyPreview() {
    GroupTrainingLobby(
        groupTrainingId = "JDq01mrnbav1pdaPuiy7",
        onDeleteClick = {}
    )
}