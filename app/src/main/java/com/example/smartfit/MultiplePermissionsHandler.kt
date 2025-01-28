package com.example.smartfit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MultiplePermissionHandler() {
    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    val visiblePermissionDialogQueue = remember { mutableStateListOf<String>() }

    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("permissions_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            var allPermissionsGranted = true
            permissionsToRequest.forEach { permission ->
                val isGranted = perms[permission] == true
                if (!isGranted) {
                    visiblePermissionDialogQueue.add(0, permission)
                }
                editor.putBoolean(permission, isGranted)
                editor.apply()
                if (!isGranted) {
                    allPermissionsGranted = false
                }
            }

            if (!allPermissionsGranted) {
                val deniedCount = sharedPreferences.getInt("denied_count", 0) + 1
                editor.putInt("denied_count", deniedCount)
                editor.apply()
                if (deniedCount >= 2) {
                    Toast.makeText(
                        context,
                        "Prosím, povol opravnenia ručne v nastaveniach aplikacie.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Prosím, udel vsetky oprávnenia pre fungovanie aplikacie.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                (context as? Activity)?.finish()
            } else {
                editor.putInt("denied_count", 0)
                editor.apply()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionsToRequest.forEach { permission ->
            editor.putBoolean(permission, false)
        }
        editor.apply()

        val permissionsToRequestAgain = permissionsToRequest.filter {
            !sharedPreferences.getBoolean(it, false)
        }.toTypedArray()
        if (permissionsToRequestAgain.isNotEmpty()) {
            multiplePermissionResultLauncher.launch(permissionsToRequestAgain)
        }
    }
}