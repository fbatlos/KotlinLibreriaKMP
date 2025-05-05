package org.example.projects.ViewModel

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    Desktop.getDesktop().browse(URI(url))
}

actual fun registerDeepLinkHandler(onDeepLinkReceived: (String) -> Unit) {
}