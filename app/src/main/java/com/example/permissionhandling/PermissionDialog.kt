package com.example.permissionhandling

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDisabled: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    goToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Divider()
                Text(
                    text = if (isPermanentlyDisabled) {
                        "Grand permission"
                    } else {
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDisabled) {
                                goToAppSettingsClick()
                            } else {
                                onClick()
                            }
                        }
                        .padding(16.dp),
                )
            }
        },
        title = {
            Text(text = "Permission Required")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDisabled = isPermanentlyDisabled,
                ),
            )
        },
        modifier = modifier,
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDisabled: Boolean): String
}

class CameraPermissionTextProvider() : PermissionTextProvider {
    override fun getDescription(isPermanentlyDisabled: Boolean): String {
        return if (isPermanentlyDisabled) {
            "You have permanently declined camera permission. " + "You can go to your app settings to grant it"
        } else {
            "This app needs access to your camera"
        }
    }
}

class RecordPermissionTextProvider() : PermissionTextProvider {
    override fun getDescription(isPermanentlyDisabled: Boolean): String {
        return if (isPermanentlyDisabled) {
            "You have permanently declined microphone permission. " + "You can go to your app settings to grant it"
        } else {
            "This app needs access to your microphone"
        }
    }
}

class PhoneCallPermissionTextProvider() : PermissionTextProvider {
    override fun getDescription(isPermanentlyDisabled: Boolean): String {
        return if (isPermanentlyDisabled) {
            "You have permanently declined call permission. " + "You can go to your app settings to grant it"
        } else {
            "This app needs access to your call permissions"
        }
    }
}
