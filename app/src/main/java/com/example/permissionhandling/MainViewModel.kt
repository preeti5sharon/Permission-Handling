package com.example.permissionhandling

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val storePermissionDialogQueue = mutableStateListOf<String>()

    fun removeDialog() {
        storePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResults(
        permission: String,
        isGranted: Boolean,
    ) {
        if (!isGranted && !storePermissionDialogQueue.contains(permission)) {
            storePermissionDialogQueue.add(permission)
        }
    }
}
