package com.epidemicsound.accountanalyser.service

import kotlin.math.log10

object BenfordDistribution {

    val digits: IntRange = 1..9

    /**
     * Expected probability of digit `d` being the leading digit, per Benford's law.
     *   P(d) = log10(1 + 1/d)
     */
    fun expectedProbability(digit: Int): Double {
        require(digit in digits) { "Benford's law is defined for digits 1..9, got $digit" }
        return log10(1.0 + 1.0 / digit)
    }

    /** Map of digit -> expected probability for digits 1..9. */
    val expectedProbabilities: Map<Int, Double> =
        digits.associateWith { expectedProbability(it) }
}
