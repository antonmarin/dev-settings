package ru.antonmarin

import org.assertj.core.api.Assertions
import org.jooq.TableRecord
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Comparator to use with AssertJ framework to compare Jooq TableRecords
 *
 * Usage:
 * ```
 * val storedRequestRecord = helper.getFirst() // get some TableRecord
 * Assertions.assertThat(storedRequestRecord)
 *     .usingComparator(JooqTableRecordComparator<entitysRecord>())
 *     .isEqualTo(entitysRecord(...)) // create expected TableRecord here
 * ```
 *
 * Equals when all fields equal, returns -1 instead
 */
class JooqTableRecordComparator<T : TableRecord<T>>(
    private val ignoringColumn: Set<String> = emptySet(),
) : java.util.Comparator<TableRecord<T>> {
    override fun compare(o1: TableRecord<T>, o2: TableRecord<T>): Int {
        o1.fields().forEach { field ->
            if (field.name !in ignoringColumn && o1.get(field.name) != o2.get(field.name)) return -1
        }
        return 0
    }
}

class JooqTableRecordComparatorTest {
    private val comparator = JooqTableRecordComparator<EntityRecord>()

    @Test
    fun `should equal when all record fields equals`() {
        val sutRecord = DataFactory.entity()

        val result = comparator.compare(sutRecord, sutRecord)

        Assertions.assertThat(result).isEqualTo(0) // assert left equals right
    }

    @Test
    fun `should not equal when any field not equals`() {
        val leftRecord = DataFactory.entity()
        val rightRecord = DataFactory.entity()

        val result = comparator.compare(leftRecord, rightRecord)

        Assertions.assertThat(result).isEqualTo(-1) // assert left not equals right
    }

    @Test
    fun `should equal when ignored fields differ`() {
        val leftRecord = DataFactory.entity()
        val rightRecord = leftRecord.copy()
        rightRecord.id = UUID.randomUUID()

        val result = JooqTableRecordComparator<EntityRecord>(ignoringColumn = setOf("id"))
            .compare(leftRecord, rightRecord)

        Assertions.assertThat(result).isEqualTo(0) // assert left equals right
    }
}
