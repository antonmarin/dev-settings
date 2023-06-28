package ru.antonmarin

import java.time.*

operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateProgression(this, other)
infix fun LocalDate.until(until: LocalDate): LocalDateProgression = this..(until.minusDays(1))

class LocalDateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = LocalDateIterator(start, endInclusive)
}

class LocalDateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
) : Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        val next = currentDate

        currentDate = currentDate.plusDays(1)

        return next
    }
}

fun LocalDate.atEndOfDay(): LocalDateTime = this.atTime(LocalTime.MAX)
fun LocalDate.atEndOfDay(zone: ZoneId): ZonedDateTime = this.atTime(LocalTime.MAX).atZone(zone)
fun LocalDate.atEndOfDay(offset: ZoneOffset): OffsetDateTime = this.atTime(LocalTime.MAX).atOffset(offset)
