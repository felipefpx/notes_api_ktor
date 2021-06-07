package me.porge.notes.api.routes

import com.google.gson.annotations.Expose

class PutPostNotesParams(
    @Expose val title: String?,
    @Expose val content: String?
)
