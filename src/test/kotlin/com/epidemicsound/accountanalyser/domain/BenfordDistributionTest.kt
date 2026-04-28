package com.epidemicsound.accountanalyser.domain

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class BenfordDistributionTest {

    @Test
    fun `digit 1 has roughly 30 percent expected probability`() {
        BenfordDistribution.expectedProbability(1) shouldBe (0.301 plusOrMinus 0.001)
    }

    @Test
    fun `expected probabilities sum to 1`() {
        val total = BenfordDistribution.expectedProbabilities.values.sum()
        total shouldBe (1.0 plusOrMinus 1e-9)
    }
}
