package org.example.projects.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.NavigationHandleDeepLink

@SuppressLint("StaticFieldLeak")
object AppContextProvider {
    lateinit var context: Context

     fun handleDeepLink(intent: Intent?) {
         val data = intent?.data?.toString()
        data?.let {
            when  {
                it.contains("/pago_exitoso") -> {
                    Log.d("DeepLink", "Pago exitoso")
                    Toast.makeText(context, "Pago exitoso", Toast.LENGTH_LONG).show()
                    NavigationHandleDeepLink.carritoViewModel.verEstadoPago(true)

                    NavigationHandleDeepLink.navigator.navigateTo(AppRoutes.Inicio)
                }
                it.contains("/pago_candelado") -> {
                    Log.d("DeepLink", "Pago cancelado")
                    Toast.makeText(context, "Pago cancelado", Toast.LENGTH_LONG).show()
                    NavigationHandleDeepLink.carritoViewModel.verEstadoPago(false)

                    NavigationHandleDeepLink.navigator.navigateTo(AppRoutes.Carrito)
                }

                else -> {
                    Toast.makeText(context, "El pago no se realizó", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}




actual fun openUrl(url: String) {
    val context = AppContextProvider.context
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    customTabsIntent.launchUrl(context, Uri.parse(url))
}



actual fun registerDeepLinkHandler(onDeepLinkReceived: (String) -> Unit) {
}