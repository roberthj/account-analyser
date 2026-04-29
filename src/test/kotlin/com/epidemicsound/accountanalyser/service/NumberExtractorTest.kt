package com.epidemicsound.accountanalyser.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NumberExtractorTest {

    private val extractor = NumberExtractor()

    @Test
    fun `picks up amount values and ignores invoice and account ids`() {
        val text = "Invoice 123 Account 456 Amount 1000.00 Notes paid... " +
            "Invoice 124 Account 789 Amount 250.50 Notes pending"

        assertEquals(listOf(1, 2), extractor.leadingDigits(text, listOf("amount")))
    }

    @Test
    fun `skips leading zeros to find first non-zero digit`() {
        assertEquals(listOf(4, 7), extractor.leadingDigits("Amount 0.0042 Amount 007", listOf("amount")))
    }

    @Test
    fun `handles negative amounts and currency markers`() {
        assertEquals(listOf(1, 9), extractor.leadingDigits("Amount: \$1234.50 Amount -987.65", listOf("amount")))
    }

    @Test
    fun `is case-insensitive on the keyword`() {
        assertEquals(listOf(5, 6), extractor.leadingDigits("amount 500 AMOUNT 600", listOf("amount")))
    }

    @Test
    fun `matches multiple keywords supplied by the caller`() {
        val text = "Total 1000 Amount 200 Sum 30"
        assertEquals(listOf(1, 2, 3), extractor.leadingDigits(text, listOf("amount", "total", "sum")))
    }

    @Test
    fun `returns empty list when text contains no matching keyword`() {
        assertEquals(emptyList<Int>(), extractor.leadingDigits("Total 100 Sum 200", listOf("amount")))
    }

    @Test
    fun `returns empty list for empty input`() {
        assertEquals(emptyList<Int>(), extractor.leadingDigits("", listOf("amount")))
    }
}
