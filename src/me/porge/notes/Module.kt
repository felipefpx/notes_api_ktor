package me.porge.notes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import me.porge.notes.api.datasources.local.Notes
import me.porge.notes.api.di.includeNotesDependencies
import me.porge.notes.api.routes.includeNotesRoutes
import me.porge.notes.helpers.HikariFactory
import me.porge.notes.helpers.connect
import me.porge.notes.helpers.prepareTables
import org.jetbrains.exposed.sql.Table
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import java.text.DateFormat

val hikariLocalDatasource = HikariFactory.create(
    jdbcUrl = EnvironmentParameters.DB_URL,
    username = EnvironmentParameters.DB_USERNAME,
    password = EnvironmentParameters.DB_PASSWORD
)

val tables = arrayOf<Table>(Notes)

@KtorExperimentalAPI
fun Application.module() {
    hikariLocalDatasource.connect()
    prepareTables(*tables)

    install(Koin) {
        modules(module {
            single<Gson> {
                GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
            }

            factory {
                HttpClient(CIO) {
                    engine {
                        requestTimeout = 30_000
                    }

                    install(Logging) {
                        logger = Logger.DEFAULT
                        level = LogLevel.BODY
                    }

                    install(JsonFeature) {
                        serializer = GsonSerializer()
                    }
                }
            }
            includeNotesDependencies()
        })
    }

    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(PartialContent)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            excludeFieldsWithoutExposeAnnotation()
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    install(Routing) {
        get {
            call.respondRedirect(EnvironmentParameters.HOMEPAGE_URL)
        }

        route("api/v1") {
            get {
                call.respondRedirect(EnvironmentParameters.HOMEPAGE_URL)
            }

            includeNotesRoutes()
        }
    }
}
