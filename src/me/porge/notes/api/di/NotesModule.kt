package me.porge.notes.api.di

import me.porge.notes.api.controllers.NotesController
import me.porge.notes.api.repositories.NotesRepository
import org.koin.core.module.Module

fun Module.includeNotesDependencies() {
    single {
        NotesRepository()
    }

    single {
        NotesController(notesRepository = get())
    }
}
