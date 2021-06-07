package me.porge.notes.api.controllers

import io.ktor.http.*
import me.porge.notes.api.repositories.NotesRepository
import java.util.*

class NotesController(
    private val notesRepository: NotesRepository
) {
    fun create(title: String, content: String): Any =
        if (title.isEmpty() || content.isEmpty()) {
            HttpStatusCode.UnprocessableEntity
        } else {
            notesRepository.createNote(
                title = title,
                content = content
            ) ?: HttpStatusCode.InternalServerError
        }

    fun put(id: String, title: String, content: String): Any =
        if (title.isEmpty() || content.isEmpty()) {
            HttpStatusCode.UnprocessableEntity
        } else {
            notesRepository.updateNote(
                id = UUID.fromString(id),
                title = title,
                content = content
            ) ?: HttpStatusCode.NotFound
        }

    fun getAll(): Any = notesRepository.getNotes()

    fun get(id: String): Any =
        notesRepository.getNote(UUID.fromString(id)) ?: HttpStatusCode.NotFound

    fun delete(id: String): Any =
        if (notesRepository.deleteNote(UUID.fromString(id)))
            HttpStatusCode.OK
        else
            HttpStatusCode.NotFound
}
