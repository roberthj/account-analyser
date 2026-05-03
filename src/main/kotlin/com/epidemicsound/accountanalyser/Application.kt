package com.epidemicsound.accountanalyser

import com.epidemicsound.accountanalyser.api.analysisRoutes
import com.epidemicsound.accountanalyser.service.AnalysisService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    //Error responses
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (cause.message ?: "Bad request")))
        }
        exception<BadRequestException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request body"))
        }
    }

    val analysisService = AnalysisService()
    analysisRoutes(analysisService)
}
