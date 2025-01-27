import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.concurrent.futures.await
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.smartfit.components.CustomAlertDialogGroupTraining
import com.example.smartfit.components.Heading1
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.User
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QrReaderScreen(
    onResult: (String) -> Unit,
    onConfirmation: (String) -> Unit,
    foundGroupTraining: GroupTraining = GroupTraining(),
    foundTrainer: User = User(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val openAlertDialog = remember { mutableStateOf(false) }


    LaunchedEffect(cameraProviderFuture) {
        cameraProvider = cameraProviderFuture.await()
    }

    when {
        openAlertDialog.value -> {
            CustomAlertDialogGroupTraining(
                onDismissRequest = {

                    openAlertDialog.value = false
                },
                onConfirmation = {
                    onConfirmation(foundGroupTraining.id)
                    openAlertDialog.value = false
                },
                groupTraining = foundGroupTraining,
                trainer = foundTrainer
            )
        }
    }

    if (permissionState.status.isGranted) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Heading1("Qr kod skener")
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back icon"
                            )
                        }
                    }
                )
            }

        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(horizontal = 15.dp, vertical = 60.dp)
                    .padding(innerPadding)
                    .clip(RoundedCornerShape(150.dp))
            ) {
                cameraProvider?.let { provider ->
                    AndroidView(factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        val imageAnalyzer = ImageAnalysis.Builder().build().also {
                            it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { result ->
                                onResult(result)
                                openAlertDialog.value = true
                            })
                        }
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        previewView
                    }, modifier = Modifier.fillMaxSize())
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Heading1("Nasmeruj kameru na qr kod", color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    } else {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

private class QrCodeAnalyzer(private val onQrCodeScanned: (String) -> Unit) :
    ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader().apply {
        setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
    }
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val source = PlanarYUVLuminanceSource(
            bytes,
            image.width,
            image.height,
            0,
            0,
            image.width,
            image.height,
            false
        )
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            val result = reader.decode(binaryBitmap)
            mainHandler.post {
                onQrCodeScanned(result.text)
            }
        } catch (e: NotFoundException) {
            // No QR code found
        } catch (e: Exception) {
            // Handle other exceptions
        } finally {
            image.close()
        }
    }
}