package dev.octoshrimpy.quik.util

import dev.octoshrimpy.quik.model.MessageCategory

object SmsCategorizer {

    private val strongTransactionPatterns = listOf(
        Regex("""\b(?:otp|one[- ]time (?:password|pin|code)|verification code|verify your|auth(?:entication)? code)\b"""),
        Regex("""\b(?:debited|credited|withdrawn|deposited|transferred|bank account|net banking)\b"""),
        Regex("""\b(?:upi|neft|imps|rtgs|transaction id|reference number|ref no)\b""")
    )

    private val transactionPatterns = listOf(
        Regex("""\b(?:balance|account|bank|a/c|card|emi|loan|invoice|receipt|bill|salary|insurance|payment|refund)\b"""),
        Regex("""(?:inr|rs\.?|usd|\$|₹)\s?\d|\d+\s?(?:inr|rs\.?|usd|dollars)""")
    )

    private val promotionPatterns = listOf(
        Regex("""\b(?:offer|offers|discount|sale|promo|promotion|coupon|cashback|deal|deals|free|win|winner|lottery|reward|rewards|bonus|gift)\b"""),
        Regex("""\b(?:limited time|buy now|shop now|order now|claim now|apply now|subscribe|unsubscribe|click here|visit us|t&c|terms and conditions)\b"""),
        Regex("""\b\d{1,3}\s?%\s?off\b"""),
        Regex("""https?://|www\.""")
    )

    private val shortCode = Regex("""^\d{5,6}$""")

    fun categorize(text: String, address: String, isKnownContact: Boolean = false): String {
        val content = text.lowercase()
        val sender = address.trim()
        val senderHasLetters = sender.any { it.isLetter() }
        val senderLetters = sender.count { it.isLetter() }
        val businessSender = !isKnownContact &&
                ((senderHasLetters && senderLetters >= 3) || shortCode.matches(sender))

        val hasStrongTransaction = strongTransactionPatterns.any { it.containsMatchIn(content) }
        val transactionScore = transactionPatterns.sumOf { pattern -> pattern.findAll(content).count() }
        val promotionScore = promotionPatterns.sumOf { pattern -> pattern.findAll(content).count() }

        return when {
            hasStrongTransaction -> MessageCategory.TRANSACTIONS
            isKnownContact -> MessageCategory.PERSONAL
            transactionScore > 0 && transactionScore >= promotionScore -> MessageCategory.TRANSACTIONS
            promotionScore > 0 -> MessageCategory.PROMOTIONS
            businessSender -> MessageCategory.OTHER
            else -> MessageCategory.PERSONAL
        }
    }
}
