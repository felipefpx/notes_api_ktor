package me.porge.notes

/**
 * This object contains some important parameters.
 */
object EnvironmentParameters {
    private const val ENV_PORT = "PORT"
    private const val ENV_DB_URL = "DATABASE_URL"
    private const val ENV_DB_USERNAME = "DATABASE_USERNAME"
    private const val ENV_DB_PASSWORD = "DATABASE_PASSWORD"

    val PORT: Int
        get() = System.getenv(ENV_PORT)?.toInt() ?: 5000

    val DB_URL: String
        get() = System.getenv(ENV_DB_URL) ?: "jdbc:postgresql://localhost:5432/notes_db"

    val DB_USERNAME: String?
        get() = System.getenv(ENV_DB_USERNAME)

    val DB_PASSWORD: String?
        get() = System.getenv(ENV_DB_PASSWORD)

    val WATCH_PATHS = listOf("heroku")

    const val HOMEPAGE_URL = "https://porge.me"
}
