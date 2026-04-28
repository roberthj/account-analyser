package com.epidemicsound.accountanalyser.domain

import io.kotest.matchers.collections.shouldContainExactly
import kotlin.test.Test

class NumberExtractorTest {

    private val extractor = NumberExtractor()

    @Test
    fun `extracts leading digits from a simple sentence`() {
        // val result = extractor.leadingDigits("invoice 123 for 45.67 USD")
        // result shouldContainExactly listOf(1, 4)
        TODO("Implement once NumberExtractor is in place")
    }

    @Test
    fun `skips leading zeros to find first non-zero digit`() {
        // val result = extractor.leadingDigits("amount 0.0042 and 007")
        // result shouldContainExactly listOf(4, 7)
        TODO()
    }

    @Test
    fun `returns empty list when text contains no numbers`() {
        // val result = extractor.leadingDigits("no digits here at all")
        // result shouldContainExactly emptyList()
        TODO()
    }
}
