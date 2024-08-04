package ru.antonmarin

import org.jooq.DSLContext
import org.jooq.TableRecord
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.fail

/**
 * Helper to insert/get jooq records.
 *
 * Usage:
 * ```
 * val helper = JooqRecordHelper<SomeTableRecord>(db:DSLContext, Tables.SOME_TABLE) // init helper for some_table table
 * helper.insert(storedTemplate) || fail("Unexpected behavior") // fail when smth breaks
 * ```
 *
 * All methods return boolean to fail when smth breaks
 */
open class JooqRecordHelper<T : TableRecord<T>>(
    private val db: DSLContext,
    private val table: TableImpl<T>,
) {
    val recordComparator = JooqTableRecordComparator<T>()

    fun getAll(): List<T> = db.selectFrom(table).fetch()
    fun findFirst(): T? = db.selectFrom(table).fetchOne()
    fun getFirst(): T = findFirst() ?: fail("Unexpected behavior")

    fun insert(record: T) = db.executeInsert(record) > 0 || fail("Unexpected behavior")
    fun insert(records: Collection<T>) = db.batchInsert(records).execute().count { it > 0 } == records.size || fail("Unexpected behavior")
    fun insert(vararg records: T) = db.batchInsert(*records).execute().count() == records.size || fail("Unexpected behavior")
}
