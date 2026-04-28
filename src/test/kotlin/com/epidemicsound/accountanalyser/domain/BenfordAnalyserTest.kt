package com.epidemicsound.accountanalyser.domain

import kotlin.test.Test

class BenfordAnalyserTest {

    private val analyser = BenfordAnalyser()

    @Test
    fun `accepts a Benford-conforming sample`() {
        // Build a sample whose leading-digit distribution roughly matches Benford
        // and assert followsBenfordsLaw == true.
        TODO()
    }

    @Test
    fun `rejects a uniform-digit sample`() {
        // A sample with each leading digit appearing equally often should NOT pass.
        TODO()
    }

    @Test
    fun `rejects significance level outside open interval 0 to 1`() {
        TODO()
    }
}
