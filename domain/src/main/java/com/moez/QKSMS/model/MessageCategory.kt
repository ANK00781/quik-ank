package dev.octoshrimpy.quik.model

object MessageCategory {
    const val ALL = "all"
    const val PERSONAL = "personal"
    const val TRANSACTIONS = "transactions"
    const val PROMOTIONS = "promotions"
    const val OTHER = "other"

    val values = listOf(ALL, PERSONAL, TRANSACTIONS, PROMOTIONS, OTHER)
}
