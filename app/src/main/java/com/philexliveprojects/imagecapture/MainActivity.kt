package com.philexliveprojects.imagecapture

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import com.philexliveprojects.imagecapture.databinding.PreviewViewBinding
import com.philexliveprojects.imagecapture.ui.theme.ImageCaptureTheme

val PERMISSIONS_GRANTED = mutableListOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCaptureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageCaptureApp()
                }
            }
        }
    }
}

@Composable
fun ImageCaptureApp(modifier: Modifier = Modifier) {
    CameraPreview(modifier)
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        AndroidViewBinding(PreviewViewBinding::inflate) {
            viewFinder.setBackgroundColor(Color(0xFF488B84).hashCode())

            val cameraProviderFeature = ProcessCameraProvider.getInstance(context)

            cameraProviderFeature.addListener({
                val cameraProvider = cameraProviderFeature.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                } catch (e: Exception) {
                    Log.e("Camera", "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageCaptureTheme {
        ImageCaptureApp()
    }
}