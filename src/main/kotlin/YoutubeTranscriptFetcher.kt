package com.example

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.collections.set

object YoutubeTranscriptFetcher {
    fun fetchYoutubeTranscript(youtubeUrl: String): List<TranscriptItem> {
        val projectDir = File(System.getProperty("user.dir"))
        val pythonExecutable = File("/opt/homebrew/bin/python3").absolutePath
        val scriptPath = File(projectDir, "scripts/youtube_transcript.py").absolutePath

        val scriptFile = File(scriptPath)
        if (!scriptFile.exists()) {
            throw RuntimeException("Python script does not exist at path: $scriptPath")
        }

        val processBuilder = ProcessBuilder(
            "/usr/bin/python3",
            scriptPath,
            youtubeUrl
        ).redirectErrorStream(true)

        // Specify UTF-8 encoding
        processBuilder.environment()["PYTHONIOENCODING"] = "utf-8"

        val process = processBuilder.start()

        val outputLines = process.inputStream.bufferedReader().readLines()
        val output = outputLines.lastOrNull()?.trim() ?: throw RuntimeException("Empty script output")

        val jsonElement = Json.parseToJsonElement(output)

        if (jsonElement is JsonObject && "error" in jsonElement) {
            val errorMessage = jsonElement["error"]?.jsonPrimitive?.content ?: "Unknown error"
            throw RuntimeException(errorMessage)
        }

        return Json.decodeFromJsonElement<List<TranscriptItem>>(jsonElement)
    }
}