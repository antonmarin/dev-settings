package ru.antonmarin

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Comparator
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Comparator to use with AssertJ framework to compare objects by public properties
 *
 * Usage:
 * ```
 * val obj1 = SomeObject() // get some object
 * val obj2 = SomeObject() // get another object
 * Assertions.assertThat(obj1)
 *     .usingComparator(PublicPropertiesComparator())
 *     .isEqualTo(obj2)
 * ```
 *
 * Equals when all fields equal, returns -1 instead
 * Requires org.jetbrains.kotlin:kotlin-reflect
 */
class PublicPropertiesComparator : Comparator<Any> {
    override fun compare(o1: Any, o2: Any): Int {
        @Suppress("UNCHECKED_CAST")
        val memberProperties = o1::class.memberProperties as Collection<KProperty1<Any, Any?>>
        memberProperties.forEach { prop ->
            if (prop.get(o1) != prop.get(o2)) return -1
        }

        return 0
    }
}


class PublicPropertiesComparatorTest {
    private val comparator = PublicPropertiesComparator()

    @Test
    fun `should equal objects when all object public properties equal`() {
        val sutObject1 = DataFactory.subscriber(1,2,3)
        val sutObject2 = DataFactory.subscriber(1,2,3)

        val result = comparator.compare(sutObject1, sutObject2)

        Assertions.assertThat(result).isEqualTo(0) // assert left equals right
    }

    @Test
    fun `should not equal objects when at least one object public properties differs`() {
        val sutObject1 = DataFactory.subscriber(subscriberId = 1)
        val sutObject2 = DataFactory.subscriber(subscriberId = 2)

        val result = comparator.compare(sutObject1, sutObject2)

        Assertions.assertThat(result).isEqualTo(-1) // assert left differs right
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
