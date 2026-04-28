package com.epidemicsound.accountanalyser

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test

class ApplicationTest {

    @Test
    fun `POST analyse returns 200 for a valid request`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) { json() }
        }
        // val response = client.post("/api/v1/analyse") {
        //     contentType(ContentType.Application.Json)
        //     setBody(AnalysisRequest(text = "...", significanceLevel = 0.05))
        // }
        // response.status shouldBe HttpStatusCode.OK
        TODO("Fill in once BenfordAnalyser.analyse is implemented")
    }
}
