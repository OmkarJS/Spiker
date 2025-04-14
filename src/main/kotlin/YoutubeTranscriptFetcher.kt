package com.example

import java.io.File
import kotlin.collections.set

object YoutubeTranscriptFetcher {
     fun fetchYoutubeTranscript(youtubeUrl: String): String {
        val projectDir = File(System.getProperty("user.dir"))
        val pythonExecutable = "C:\\Users\\krishna.ext_alten\\AppData\\Local\\Programs\\Python\\Python313\\python.exe"
        val scriptPath = File(projectDir, "scripts/youtube_transcript.py").absolutePath

        val scriptFile = File(scriptPath)
        if (!scriptFile.exists()) {
            throw RuntimeException("Python script does not exist at path: $scriptPath")
        }

        val processBuilder = ProcessBuilder(
            pythonExecutable,
            scriptPath,
            youtubeUrl
        ).redirectErrorStream(true)

         // Specify UTF-8 encoding
        processBuilder.environment()["PYTHONIOENCODING"] = "utf-8"

        val process = processBuilder.start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()

        if (exitCode != 0) {
            throw RuntimeException("Python script execution failed: $output")
        }

        return output
    }
}