package org.example.projects.BaseDeDatos.model

data class Compra(
    val usuarioId: String,
    val items: List<CompraItem>,
    val metodoDePago: MetodoDePago
)


data class CompraItem(
    val libroId: String,
    val precio: Double,
    val type: TipoStock
)


data class MetodoDePago(
    val token: String,
    val tipo: TipoDePago
)
enum class TipoDePago {
    STRIPE_CARD, GOOGLE_PAY, APPLE_PAY
}