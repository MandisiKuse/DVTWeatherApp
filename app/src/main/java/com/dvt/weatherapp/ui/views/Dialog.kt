package com.dvt.weatherapp.ui.views

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun Dialog(
    title: String,
    message: String,
    positiveButtonTitle: String,
    negativeButtonTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onConfirm()
                    }
                ) {
                    Text(positiveButtonTitle)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text(negativeButtonTitle)
                }
            }
        )
    }
}