package me.porge.notes

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*

@KtorExperimentalAPI
fun main() {
    embeddedServer(
        factory = Netty,
        port = EnvironmentParameters.PORT,
        watchPaths = EnvironmentParameters.WATCH_PATHS,
        module = Application::module
    ).start(wait = true)
}
