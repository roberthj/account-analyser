package com.epidemicsound.accountanalyser.api

import kotlinx.serialization.Serializable

@Serializable
data class AnalysisRequest(
    val text: String,
    val significanceLevel: Double,
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
    val significanceLevel: Double,
    val pValue: Double,
    val followsBenfordsLaw: Boolean,
    val distribution: List<DigitDistribution>,
)
