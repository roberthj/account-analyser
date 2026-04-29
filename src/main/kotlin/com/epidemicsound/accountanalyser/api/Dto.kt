package com.epidemicsound.accountanalyser.api

import kotlinx.serialization.Serializable

@Serializable
data class AnalysisRequest(
    val text: String,
    val significanceLevel: Double,
    // Keywords that label the amounts to extract. Null/empty falls back to a default
    val amountKeywords: List<String>? = null,
)

@Serializable
data class DigitDistribution(
    val digit: Int,
    val expectedFrequency: Double,
    val actualFrequency: Double,
)

@Serializable
data class AnalysisResponse(
    val sampleSize: Int,
    val expectedSignificanceLevel: Double,
    val observedSignificanceLevel: Double,
    val followsBenfordsLaw: Boolean,
    val distribution: List<DigitDistribution>,
)
