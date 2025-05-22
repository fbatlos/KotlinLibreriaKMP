// commonMain/src/commonMain/kotlin/your/package/theme/AppColors.kt
import androidx.compose.ui.graphics.Color

object AppColors {
    // Neutral Colors
    val black = Color(0xFF263238)
    val darkGrey = Color(0xFF4D4D4D)
    val grey = Color(0xFF717171)
    val lightGrey = Color(215,215,215)
    val greyBlue = Color(0xFFABBED1)
    val silver = Color(0xFFFFFEFA)
    val white = Color(0xFFFFFFFF)

    // Primary Colors
    val primary = Color(21,148,68)//Color(0xFF4CAF50) // Nota: Este color tiene solo 5 dígitos, debería ser 6 u 8
    val secondary = Color(0xFFA5D6A7)
    val info = Color(0xFF219413)


    // Action Colors
    val warning = Color(0xFFFBC02D)
    val error = Color(0xFFE53835)
    val success = Color(0xFF2E7D31) // Corregido de #ZE7D31 a valor válido
}