package com.epidemicsound.accountanalyser.api

import com.epidemicsound.accountanalyser.domain.BenfordAnalyser
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.analysisRoutes(
    analyser: BenfordAnalyser = BenfordAnalyser(),
) {
    routing {
        post("/api/v1/analyse") {
            val request = call.receive<AnalysisRequest>()
            val result = analyser.analyse(request.text, request.significanceLevel)
            call.respond(result)
        }
    }
}
