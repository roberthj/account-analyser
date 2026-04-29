package com.epidemicsound.accountanalyser.api

import com.epidemicsound.accountanalyser.module
import com.epidemicsound.accountanalyser.service.BenfordDistribution
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AnalysisRouteTest {

    //TODO: Clean up tests
    @Test
    fun `POST analyse returns 200 and a Benford verdict for a valid request`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val text = buildString {
            for ((digit, probability) in BenfordDistribution.expectedProbabilities) {
                repeat((probability * 1000).toInt()) {
                    append("Amount ").append(digit).append("00 ")
                }
            }
        }

        val response = client.post("/api/v1/analyse") {
            contentType(ContentType.Application.Json)
            setBody(AnalysisRequest(text = text, significanceLevel = 0.05))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<AnalysisResponse>()
        assertTrue(body.followsBenfordsLaw)
        assertEquals(9, body.distribution.size)
    }

    @Test
    fun `POST analyse returns 400 when input has no amounts`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/v1/analyse") {
            contentType(ContentType.Application.Json)
            setBody(AnalysisRequest(text = "no amounts here", significanceLevel = 0.05))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 1.0, -0.1, 1.5])
    fun `POST analyse returns 400 for significance level outside open interval`(level: Double) = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/v1/analyse") {
            contentType(ContentType.Application.Json)
            setBody(AnalysisRequest(text = "Amount 100 Amount 200", significanceLevel = level))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST analyse honours caller-supplied amountKeywords`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/v1/analyse") {
            contentType(ContentType.Application.Json)
            setBody(
                AnalysisRequest(
                    text = "Total 100 Total 200 Sum 300 Amount 999",
                    significanceLevel = 0.05,
                    amountKeywords = listOf("total", "sum"),
                ),
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<AnalysisResponse>()
        assertEquals(3, body.sampleSize)
    }

    @Test
    fun `POST analyse matches keywords case-insensitively`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/v1/analyse") {
            contentType(ContentType.Application.Json)
            setBody(
                AnalysisRequest(
                    text = "amount 100 AMOUNT 200 Amount 300",
                    significanceLevel = 0.05,
                    amountKeywords = listOf("amount"),
                ),
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, response.body<AnalysisResponse>().sampleSize)
    }

}
