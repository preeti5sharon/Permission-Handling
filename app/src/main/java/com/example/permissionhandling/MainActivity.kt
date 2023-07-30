package com.example.permissionhandling

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permissionhandling.ui.theme.PermissionHandlingTheme

class MainActivity : ComponentActivity() {

    private val permissionToRequest = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlingTheme {
                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.storePermissionDialogQueue

                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted: Boolean ->
                        viewModel.onPermissionResults(
                            permission = Manifest.permission.CAMERA,
                            isGranted = isGranted,
                        )
                    },
                )
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { permis ->
                        permissionToRequest.forEach { permission ->
                            viewModel.onPermissionResults(
                                permission = permission,
                                isGranted = permis[permission] == true,
                            )
                        }
                    },
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = {
                        cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text(text = "Request one permission dialog")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick =
                        {
                            multiplePermissionResultLauncher.launch(
                                permissionToRequest,
                            )
                        },
                    ) {
                        Text(text = "Request multiple permission dialog")
                    }
                }
                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.CAMERA -> {
                                    CameraPermissionTextProvider()
                                }
                                Manifest.permission.RECORD_AUDIO -> {
                                    RecordPermissionTextProvider()
                                }
                                Manifest.permission.CALL_PHONE -> {
                                    PhoneCallPermissionTextProvider()
                                }
                                else -> return@forEach
                            },
                            isPermanentlyDisabled = !shouldShowRequestPermissionRationale(permission),
                            onDismiss = viewModel::removeDialog,
                            onClick = {
                                viewModel.removeDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission),
                                )
                            },
                            goToAppSettingsClick = ::openAppSettings,
                        )
                    }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    ).also(::startActivity)
}
