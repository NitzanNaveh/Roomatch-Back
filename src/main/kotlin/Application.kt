package com

import com.utils.configureMiddleware
import io.ktor.server.application.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopping


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {
    try {
        val dotenv = dotenv()
        println("Gemini API Key Loaded: ${dotenv["GEMINI_API_KEY"]?.take(4)}***")
        val config = environment.config
        println("▶▶ Host: ${config.propertyOrNull("ktor.deployment.host")?.getString()}")
        println("▶▶ Port: ${config.propertyOrNull("ktor.deployment.port")?.getString()}")

        environment.monitor.subscribe(ApplicationStarted) {
            println("🚀 Application started.")
        }

        environment.monitor.subscribe(ApplicationStopping) {
            println("🛑 Application is stopping.")
        }

        configureSerialization()
        configureMiddleware()
        configureHTTP()
        configureRouting()
    } catch (e: Exception) {
        println("❌ Exception in Application.module: ${e.message}")
        e.printStackTrace()
    }
}
