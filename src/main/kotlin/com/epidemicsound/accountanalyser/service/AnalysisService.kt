package com.epidemicsound.accountanalyser.service

import com.epidemicsound.accountanalyser.api.AnalysisResponse
import com.epidemicsound.accountanalyser.api.DigitDistribution
import org.apache.commons.math3.stat.inference.ChiSquareTest

class AnalysisService(
    private val extractor: NumberExtractor = NumberExtractor(),
    private val chiSquareTest: ChiSquareTest = ChiSquareTest(),
) {

    companion object {
        private val DEFAULT_KEYWORDS = listOf("amount")
    }

    /**
     * Run a Benford's-law check on `text` at the given significance level.
     *
     * `amountKeywords` selects which labelled values to extract — null or an
     * empty list falls back to [DEFAULT_KEYWORDS]. (Future work: when keywords
     * are absent, fall back to extracting all numbers.)
     */
    fun analyse(
        text: String,
        expectedSignificanceLevel: Double,
        amountKeywords: List<String>? = null,
    ): AnalysisResponse {

        require(expectedSignificanceLevel > 0.0 && expectedSignificanceLevel < 1.0) {
            "significanceLevel must be in the open interval (0, 1), got $expectedSignificanceLevel"
        }

        val keywords = amountKeywords?.takeIf { it.isNotEmpty() } ?: DEFAULT_KEYWORDS

        val digits = extractor.leadingDigits(text, keywords)
        require(digits.isNotEmpty()) { "No amounts found in input text" }

        val sampleSize = digits.size
        val observedCounts: Map<Int, Int> = digits.groupingBy { it }.eachCount()

        //Format for ChiSquareTest
        val expected = BenfordDistribution.digits
            .map { d -> BenfordDistribution.expectedProbability(d) * sampleSize }
            .toDoubleArray()

        val observedSignificanceLevel = chiSquareTest.chiSquareTest(expected, toCountArray(observedCounts))

        //TODO: Review this Response
        return AnalysisResponse(
            sampleSize = sampleSize,
            expectedSignificanceLevel = expectedSignificanceLevel,
            observedSignificanceLevel = observedSignificanceLevel,
            followsBenfordsLaw = observedSignificanceLevel >= expectedSignificanceLevel,
            distribution = BenfordDistribution.digits.map { d ->
                DigitDistribution(
                    digit = d,
                    expectedFrequency = BenfordDistribution.expectedProbability(d),
                    actualFrequency = (observedCounts[d] ?: 0).toDouble() / sampleSize,
                )
            },
        )
    }

    // Make index 0..8 correspond to digits 1..9, filling in 0 for any missing digits
    private fun toCountArray(counts: Map<Int, Int>): LongArray =
        BenfordDistribution.digits
            .map { d -> (counts[d] ?: 0).toLong() }
            .toLongArray()

}
