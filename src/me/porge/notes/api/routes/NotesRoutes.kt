package me.porge.notes.api.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.porge.notes.api.controllers.NotesController
import me.porge.notes.helpers.KoinInjector

fun Route.includeNotesRoutes() {
    route("notes") {
        post {
            call.receive<PutPostNotesParams>().let { params ->
                KoinInjector.koinGet<NotesController>().create(
                    title = params.title!!,
                    content = params.content!!
                ).let { call.respond(it) }
            }
        }

        get {
            KoinInjector.koinGet<NotesController>()
                .getAll()
                .let { call.respond(it) }
        }

        get("{id}") {
            KoinInjector.koinGet<NotesController>()
                .get(call.parameters["id"]!!.toString())
                .let { call.respond(it) }
        }

        put("{id}") {
            call.receive<PutPostNotesParams>().let { params ->
                KoinInjector.koinGet<NotesController>().put(
                    id = call.parameters["id"]!!.toString(),
                    title = params.title!!,
                    content = params.content!!
                ).let { call.respond(it) }
            }
        }

        delete("{id}") {
            KoinInjector.koinGet<NotesController>()
                .delete(call.parameters["id"]!!.toString())
                .let { call.respond(it) }
        }
    }
}
