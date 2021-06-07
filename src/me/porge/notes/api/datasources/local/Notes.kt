package me.porge.notes.api.datasources.local

import org.jetbrains.exposed.sql.Table

object Notes : Table("notes") {
    val id = uuid("id").uniqueIndex()
    val title = varchar("title", 256)
    val content = varchar("content", 2056)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
