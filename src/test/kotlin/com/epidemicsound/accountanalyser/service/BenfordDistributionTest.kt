package com.epidemicsound.accountanalyser.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BenfordDistributionTest {

    @Test
    fun `digit 1 has roughly 30 percent expected probability`() {
        assertEquals(0.301, BenfordDistribution.expectedProbability(1), 0.001)
    }

    @Test
    fun `expected probabilities sum to 1`() {
        val total = BenfordDistribution.expectedProbabilities.values.sum()
        assertEquals(1.0, total, 1e-9)
    }
}
