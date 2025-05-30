package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        spikingRoute()
        fetchTranscriptRoute()
    }
}

private fun Routing.spikingRoute() {
    get("/") {
        call.respondText("Spiking...")
    }
}

private fun Routing.fetchTranscriptRoute() {
    post("/transcript") {
        try {
            val request = call.receive<TranscriptRequest>()
            val youtubeUrl = request.youtubeUrl

            val transcript = YoutubeTranscriptFetcher.fetchYoutubeTranscript(youtubeUrl)
            call.respond(TranscriptResponse(transcript = transcript))
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, TranscriptResponse(error = "Invalid JSON format: ${e.message}"))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError,
                TranscriptResponse(error = "Failed to fetch transcript: ${e.message}")
            )
        }
    }
}

@Serializable
data class TranscriptRequest(val youtubeUrl: String)

@Serializable
data class TranscriptResponse(val transcript: String = "", val error: String = "")