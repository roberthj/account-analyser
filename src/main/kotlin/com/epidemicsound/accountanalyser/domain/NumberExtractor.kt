package com.epidemicsound.accountanalyser.domain

class NumberExtractor {

    /**
     * Extract numbers from a free-form string and return their leading (most significant) digits.
     * Leading digit is always in 1..9 (zeros are skipped — Benford's law is defined over 1..9).
     *
     * Decisions you'll need to make:
     *  - Which token shapes count as "a number"? Integers? Decimals? Negatives? Thousand separators?
     *  - What about "0.0042" — leading digit is 4.
     *  - What about "007" — leading non-zero digit is 7.
     */
    fun leadingDigits(text: String): List<Int> {
        TODO("Parse `text`, extract numeric tokens, and return their leading digits in 1..9")
    }
}
