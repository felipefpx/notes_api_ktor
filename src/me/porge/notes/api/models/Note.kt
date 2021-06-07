package me.porge.notes.api.models

import com.google.gson.annotations.Expose
import java.util.*

class Note(
    @Expose val id: UUID,
    @Expose val title: String,
    @Expose val content: String,
    @Expose val updatedAt: Long
)
