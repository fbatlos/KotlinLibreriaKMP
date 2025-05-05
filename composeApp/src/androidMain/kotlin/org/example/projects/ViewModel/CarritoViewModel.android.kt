package org.example.projects.ViewModel

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


actual fun openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

actual fun registerDeepLinkHandler(onDeepLinkReceived: (String) -> Unit) {
}