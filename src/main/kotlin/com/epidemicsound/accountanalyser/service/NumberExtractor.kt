package com.epidemicsound.accountanalyser.service

/**
 * Extracts amount values from semi-structured financial text and returns their
 * leading (first non-zero) digits.
 *
 * The caller supplies the keyword(s) that label the amounts — e.g.
 * `["amount"]` matches "Amount 1000.00", `["amount", "total"]` also matches
 * "Total: $250". Keywords are matched case-insensitively
 *
 * Unlabelled numbers (invoice IDs, account IDs, free-text digits) are
 * ignored
 */
class NumberExtractor {

    fun leadingDigits(text: String, keywords: List<String>): List<Int> =
        buildAmountRegex(keywords)
            .findAll(text)
            .mapNotNull { match -> leadingDigit(match.groupValues[1]) }
            .toList()

    private fun buildAmountRegex(keywords: List<String>): Regex {
        val keywordsStr = keywords.joinToString("|") { Regex.escape(it) }
        return Regex("""(?i)\b(?:$keywordsStr)[\s:]+\$?(-?\d+(?:[.,]\d+)?)""")
    }

    private fun leadingDigit(amount: String): Int? =
        amount.firstOrNull { it in '1'..'9' }?.digitToInt()
}

//TODO: Add fallback for more unstructured input
// For example numbers with $100, €100, 100.00, , sum, tot, etc to filter out irrelevant numbers
