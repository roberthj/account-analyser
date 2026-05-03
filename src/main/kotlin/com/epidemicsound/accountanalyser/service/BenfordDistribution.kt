package com.epidemicsound.accountanalyser.service

import kotlin.math.log10

object BenfordDistribution {

    val digits: IntRange = 1..9

    //Expected probability of digit `d` being the leading digit, using Benford's law.
    fun expectedProbability(digit: Int): Double {
        return log10(1.0 + 1.0 / digit)
    }

    //Map of digit -> expected probability for digits 1..9.
    val expectedProbabilities: Map<Int, Double> =
        digits.associateWith { expectedProbability(it) }
}
