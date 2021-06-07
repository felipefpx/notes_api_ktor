package me.porge.notes.helpers

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Prepare exposed [tables].
 */
fun <T : Table> prepareTables(vararg tables: T) = transaction {
    SchemaUtils.createMissingTablesAndColumns(*tables)
}

fun <T : Table> resetTables(vararg tables: T) = transaction {
    SchemaUtils.drop(*tables)
    SchemaUtils.create(*tables)
}

class UpsertStatement<Key : Any>(table: Table, conflictColumn: Column<*>? = null, conflictIndex: Index? = null) :
    InsertStatement<Key>(table, false) {

    private val indexName: String
    private val indexColumns: List<Column<*>>
    private val index: Boolean

    init {
        when {
            conflictIndex != null -> {
                index = true
                indexName = conflictIndex.indexName
                indexColumns = conflictIndex.columns
            }
            conflictColumn != null -> {
                index = false
                indexName = conflictColumn.name
                indexColumns = listOf(conflictColumn)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun prepareSQL(transaction: Transaction) = buildString {
        append(super.prepareSQL(transaction))

        val dialect = transaction.db.vendor
        if (dialect == "postgresql") {
            if (index) {
                append(" ON CONFLICT ON CONSTRAINT ")
                append(indexName)
            } else {
                append(" ON CONFLICT(")
                append(indexName)
                append(")")
            }
            append(" DO UPDATE SET ")

            values.keys.filter { it !in indexColumns }
                .joinTo(this) { "${transaction.identity(it)}=EXCLUDED.${transaction.identity(it)}" }

        } else {

            append(" ON DUPLICATE KEY UPDATE ")
            values.keys.filter { it !in indexColumns }
                .joinTo(this) { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }

        }
    }

}

inline fun <T : Table> T.upsert(
    conflictColumn: Column<*>? = null,
    conflictIndex: Index? = null,
    body: T.(UpsertStatement<Number>) -> Unit
) =
    UpsertStatement<Number>(this, conflictColumn, conflictIndex).apply {
        body(this)
        execute(TransactionManager.current())
    }

fun Table.indexR(customIndexName: String? = null, isUnique: Boolean = false, vararg columns: Column<*>): Index {
    val index = Index(columns.toList(), isUnique, customIndexName)
    indices.add(index)
    return index
}

fun Table.uniqueIndexR(customIndexName: String? = null, vararg columns: Column<*>): Index =
    indexR(customIndexName, true, *columns)
