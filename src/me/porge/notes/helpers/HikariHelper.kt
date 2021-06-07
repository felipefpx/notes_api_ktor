package me.porge.notes.helpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object HikariFactory {
    fun create(jdbcUrl: String, username: String?, password: String?): HikariDataSource {
        val config = HikariConfig().also { cfg ->
            cfg.jdbcUrl = jdbcUrl.toJdbcUrl()
            cfg.driverClassName = "org.postgresql.ds.PGSimpleDataSource"
            username?.let { cfg.username = it }
            password?.let { cfg.password = it }
        }
        return HikariDataSource(config).apply {
            println(driverClassName)
        }
    }

    private fun String.toJdbcUrl(): String {
        val jdbcPrefix = "jdbc:postgresql://"
        val url = replace("(jdbc:)?postgres(ql)?://".toRegex(), "")

        if (url.contains("@")) {
            val urlParts = url.split("@")
            val userCredentials = urlParts[0].split(":")
            return "$jdbcPrefix${urlParts[1]}?user=${userCredentials[0]}&password=${userCredentials[1]}&sslmode=require"
        }

        return "$jdbcPrefix$url"
    }

}

fun HikariDataSource.connect() = Database.connect(this)
