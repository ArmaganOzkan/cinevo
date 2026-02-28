package com.armagan.cinevo.ui.customcomposables

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.armagan.cinevo.R

@Composable
fun ExpandablePoster(imageUrl: String,title:String) {
    var showDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AsyncImage(
        model = imageUrl,
        contentDescription = "Poster",
        modifier = Modifier
            .size(width = 180.dp, height = 240.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { showDialog = true },
        contentScale = ContentScale.Crop
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Expanded Poster",
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.95f)
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { showDialog = false },
                                onLongPress = { showDownloadDialog = true }
                            )
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }


    if (showDownloadDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = { Text(stringResource(id= R.string.downloadimage)) },
            text = { Text(stringResource(id = R.string.askdownload)) },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    val request = DownloadManager.Request(Uri.parse(imageUrl))
                        .setTitle("${title}.jpg")
                        .setDescription("Downloading poster")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${title}.jpg")

                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)
                    showDownloadDialog = false
                }) {
                    Text(stringResource(id=R.string.yes))
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDownloadDialog = false }) {
                    Text(stringResource(id=R.string.no))
                }
            }
        )
    }
}
