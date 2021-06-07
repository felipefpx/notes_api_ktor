package me.porge.notes.api.datasources.local

import me.porge.notes.api.models.Note
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toNote() = Note(
    id = this[Notes.id],
    title = this[Notes.title],
    content = this[Notes.content],
    updatedAt = this[Notes.updatedAt]
)
