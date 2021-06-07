package me.porge.notes.api.repositories

import me.porge.notes.api.datasources.local.Notes
import me.porge.notes.api.datasources.local.toNote
import me.porge.notes.api.models.Note
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class NotesRepository {
    fun createNote(
        title: String,
        content: String,
        timeInMillis: Long = Calendar.getInstance().timeInMillis
    ): Note? {
        val id = UUID.randomUUID()
        return transaction {
            Notes
                .insert { noteRow ->
                    noteRow[Notes.id] = id
                    noteRow[Notes.title] = title
                    noteRow[Notes.content] = content
                    noteRow[createdAt] = timeInMillis
                    noteRow[updatedAt] = timeInMillis
                }

            commit()

            Notes
                .select { Notes.id eq id }
                .firstOrNull()
                ?.toNote()
        }
    }

    fun updateNote(
        id: UUID,
        title: String,
        content: String,
        timeInMillis: Long = Calendar.getInstance().timeInMillis
    ): Note? {
        return transaction {
            Notes
                .update({ Notes.id eq id }) { noteRow ->
                    noteRow[Notes.title] = title
                    noteRow[Notes.content] = content
                    noteRow[updatedAt] = timeInMillis
                }

            commit()

            Notes
                .select { Notes.id eq id }
                .firstOrNull()
                ?.toNote()
        }
    }

    fun getNote(
        id: UUID,
    ): Note? {
        return transaction {
            Notes
                .select { Notes.id eq id }
                .firstOrNull()
                ?.toNote()
        }
    }

    fun deleteNote(
        id: UUID,
    ): Boolean {
        return transaction {
            Notes.deleteWhere { Notes.id eq id } > 0
        }
    }

    fun getNotes(): List<Note> {
        return transaction {
            Notes
                .selectAll()
                .map { it.toNote() }
        }
    }
}
