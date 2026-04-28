package com.epidemicsound.accountanalyser.domain

import com.epidemicsound.accountanalyser.api.AnalysisResponse
import com.epidemicsound.accountanalyser.api.DigitDistribution
import org.apache.commons.math3.stat.inference.ChiSquareTest

class BenfordAnalyser(
    private val extractor: NumberExtractor = NumberExtractor(),
    private val chiSquareTest: ChiSquareTest = ChiSquareTest(),
) {

    /**
     * Run a Benford's-law goodness-of-fit check on `text`.
     *
     * Steps:
     *  1. Extract leading digits from `text` via [NumberExtractor].
     *  2. Count observed frequencies for digits 1..9.
     *  3. Compute expected counts via [BenfordDistribution] scaled to the sample size.
     *  4. Use Apache Commons Math `ChiSquareTest.chiSquareTest(expected, observed)` to get a p-value.
     *  5. Decide: reject null (does NOT follow Benford) iff p < significanceLevel.
     *
     * Edge cases worth thinking about:
     *  - Empty input / no numbers found → bail out with a clear error.
     *  - Very small samples → chi-square is unreliable; consider warning or refusing.
     *  - significanceLevel out of (0, 1) → reject the request.
     */
    fun analyse(text: String, significanceLevel: Double): AnalysisResponse {
        TODO("Wire NumberExtractor + BenfordDistribution + ChiSquareTest and build an AnalysisResponse")
    }
}
