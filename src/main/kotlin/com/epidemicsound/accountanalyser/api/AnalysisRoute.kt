package com.epidemicsound.accountanalyser.api

import com.epidemicsound.accountanalyser.service.AnalysisService
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.analysisRoutes(
    analysisService: AnalysisService = AnalysisService(),
) {
    routing {
        post("/api/v1/analyse") {
            val request = call.receive<AnalysisRequest>()
            val result = analysisService.analyse(request.text, request.significanceLevel, request.amountKeywords)
            call.respond(result)
        }
    }
}
