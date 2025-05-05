// commonMain/src/commonMain/kotlin/your/package/theme/AppColors.kt
import androidx.compose.ui.graphics.Color

object AppColors {
    // Neutral Colors
    val black = Color(0xFF263238)
    val darkGrey = Color(0xFF4D4D4D)
    val grey = Color(0xFF717171)
    val lightGrey = Color(0xFF89939E)
    val greyBlue = Color(0xFFABBED1)
    val silver = Color(0xFFFFFEFA)
    val white = Color(0xFFFFFFFF)

    // Primary Colors
    val primary = Color(0xFF4CAF50) // Nota: Este color tiene solo 5 dígitos, debería ser 6 u 8
    val secondary = Color(0xFF263238)
    val info = Color(0xFF219413)

    // Primary Shades
    val shade1 = Color(0xFF43A04B)
    val shade2 = Color(0xFF38BE3B)
    val shade3 = Color(0xFF237D31)
    val shade4 = Color(0xFF1B5E1F) // Corregido de #IBSEIF a valor válido
    val shade5 = Color(0xFF103E13)

    // Primary Tints
    val tint1 = Color(0xFF68BBB9)
    val tint2 = Color(0xFF81C784)
    val tint3 = Color(0xFFA5D6A7) // Corregido de #ASD6A7 a valor válido
    val tint4 = Color(0xFFC8E6C9) // Corregido de #CBEEQ9 a valor válido
    val tint5 = Color(0xFFEBF5E9)

    // Action Colors
    val warning = Color(0xFFFBC02D)
    val error = Color(0xFFE53835)
    val success = Color(0xFF2E7D31) // Corregido de #ZE7D31 a valor válido

    // Corrección para el color primary (asumiendo que faltaba un dígito)
    val correctedPrimary = Color(0xFF2BCBB0) // Ejemplo, ajusta según lo necesario
}