package com.epidemicsound.accountanalyser.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AnalysisServiceTest {

    //TODO: Clean up tests
    private val analysisService = AnalysisService()
    private val significanceLevel = 0.05

    @Test
    fun `accepts a sample whose digit frequencies match Benford`() {
        val text = textWithLeadingDigitFrequencies(
            sampleSize = 1000,
            frequencies = BenfordDistribution.expectedProbabilities,
        )

        val result = analysisService.analyse(text, significanceLevel)

        assertTrue(result.followsBenfordsLaw)
        assertEquals(9, result.distribution.size)
    }

    @Test
    fun `rejects a sample with uniformly distributed leading digits`() {
        val uniform = BenfordDistribution.digits.associateWith { 1.0 / 9.0 }
        val text = textWithLeadingDigitFrequencies(sampleSize = 900, frequencies = uniform)

        val result = analysisService.analyse(text, significanceLevel)

        assertFalse(result.followsBenfordsLaw)
    }

    @Test
    fun `response distribution is ordered 1 through 9 and frequencies sum to 1`() {
        val text = textWithLeadingDigitFrequencies(
            sampleSize = 200,
            frequencies = BenfordDistribution.expectedProbabilities,
        )

        val result = analysisService.analyse(text, significanceLevel)

        assertEquals((1..9).toList(), result.distribution.map { it.digit })
        assertEquals(1.0, result.distribution.sumOf { it.actualFrequency }, 1e-9)
    }

    @Test
    fun `rejects input with no amounts`() {
        assertThrows(IllegalArgumentException::class.java) {
            analysisService.analyse("Invoice 1 Account 2 just text no amounts", significanceLevel)
        }
    }

    @Test
    fun `respects caller-supplied keywords`() {
        val text = "Total 100 Total 200 Total 300"
        val result = analysisService.analyse(text, significanceLevel, amountKeywords = listOf("total"))
        assertEquals(3, result.sampleSize)
    }

    @Test
    fun `falls back to default keyword when amountKeywords is null or empty`() {
        val text = "Amount 100 Amount 200 Amount 300"

        val nullResult = analysisService.analyse(text, significanceLevel, amountKeywords = null)
        val emptyResult = analysisService.analyse(text, significanceLevel, amountKeywords = emptyList())

        assertEquals(3, nullResult.sampleSize)
        assertEquals(3, emptyResult.sampleSize)
    }

    /**
     * Builds a text whose `Amount` tokens have leading digits matching the given
     * frequency map (rounded to integer counts that sum to ~sampleSize).
     */
    private fun textWithLeadingDigitFrequencies(
        sampleSize: Int,
        frequencies: Map<Int, Double>,
    ): String = buildString {
        for ((digit, probability) in frequencies) {
            val count = (probability * sampleSize).toInt()
            repeat(count) {
                append("Amount ").append(digit).append("00 ")
            }
        }
    }
}
