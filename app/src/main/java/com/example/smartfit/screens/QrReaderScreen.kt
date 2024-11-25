import android.Manifest
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.concurrent.futures.await
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.smartfit.components.CustomAlertDialogGroupTraining
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


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrReaderScreen(
    onResult: (String) -> Unit,
    foundGroupTraining: GroupTraining = GroupTraining(),
    foundTrainer: User = User()
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


//    if ((foundTrainer != User()) && (foundGroupTraining != GroupTraining())) {
//        Log.d("ahoj", "tu som: $foundGroupTraining, $foundTrainer")
//        openAlertDialog.value = true
//    }
    when {
        openAlertDialog.value -> {
            CustomAlertDialogGroupTraining(
                onDismissRequest = {

                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                groupTraining = foundGroupTraining,
                trainer = foundTrainer
            )
        }
    }

    if (permissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
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