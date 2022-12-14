package parse.dont.validate

import parse.dont.validate.NIR.Sex.Parser.parseSex
import parse.dont.validate.NIR.Year.Parser.parseYear
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

private fun <T> String.parseOrNull(
    isValid: (Int) -> Boolean,
    factory: (Int) -> T
): T? = this.toIntOrNull()?.let { if (isValid(it)) factory(it) else null }

private fun Int.toString2(): String = String.format("%02d", this)
private fun Int.toString3(): String = String.format("%03d", this)

data class NIR(
    val sex: Sex,
    val year: Year,
    val month: Month,
    val department: Department,
    val city: City,
    val serialNumber: SerialNumber
) {
    override fun toString(): String = toLong().toString() + key().toString2()
    private fun toLong(): Long = (sex.toString() + year + month + department + city + serialNumber).toLong()
    fun key(): Int = (97 - (toLong() % 97)).toInt()

    enum class Sex {
        M, F;

        override fun toString(): String = if (this == NIR.Sex.M) "1" else "2"

        companion object Parser {
            fun parseSex(input: Char): Sex? =
                when (input) {
                    '1' -> M
                    '2' -> F
                    else -> null
                }
        }
    }

    @JvmInline
    value class Year(private val value: Int) {
        init {
            require(value.isValid())
        }

        override fun toString(): String = value.toString2()

        companion object Parser {
            private fun Int.isValid() = this in 0..99
            fun parseYear(input: String): Year? = input.parseOrNull({ it.isValid() }, ::Year)
        }
    }

    enum class Month {
        Jan, Feb, Mar, Apr, May, Jun, Jul, Aou, Sep, Oct, Nov, Dec;

        override fun toString(): String = (this.ordinal + 1).toString2()

        companion object Parser {
            fun parseMonth(input: String): Month? =
                input.toIntOrNull()
                    ?.let {
                        return Month.values()
                            .find { m -> m.ordinal == it - 1 }
                    }
        }
    }

    @JvmInline
    value class Department(private val value: Int) {
        init {
            require(value.isValid())
        }

        override fun toString(): String = value.toString2()

        companion object Parser {
            private fun Int.isValid() = this in 1..95 || this == 99
            fun parseDepartment(input: String): Department? = input.parseOrNull({ it.isValid() }, ::Department)
        }
    }

    @JvmInline
    value class City(private val value: Int) {
        init {
            require(value.isValid())
        }

        override fun toString(): String = value.toString3()

        companion object Parser {
            private fun Int.isValid() = this in 1..999

            fun parseCity(input: String): City? = input.parseOrNull({ it.isValid() }, ::City)
        }
    }

    @JvmInline
    value class SerialNumber(private val value: Int) {
        init {
            require(value.isValid())
        }

        override fun toString(): String = value.toString3()

        companion object Parser {
            private fun Int.isValid() = this in 1..999

            fun parseSerialNumber(input: String): SerialNumber? = input.parseOrNull({ it.isValid() }, ::SerialNumber)
        }
    }

    companion object Parser {
        private const val validLength = 15

        fun parseNIR(input: String): Result<NIR> =
            if (input.length != validLength) failure(ParsingException("Not a valid NIR: should have a length of $validLength"))
            else parseSafely(input)

        private fun NIR.checkKey(input: String): Boolean =
            input.substring(13)
                .toIntOrNull()
                .let { return this.key() == it }

        private fun parseSafely(input: String): Result<NIR> {
            val nir = NIR(
                sex = parseSex(input[0]) ?: return "Not a valid sex".toFailure(),
                year = parseYear(input.substring(1).take(2))
                    ?: return "Year should be positive and lt 100".toFailure(),
                month = Month.parseMonth(input.substring(3).take(2)) ?: return "Not a valid month".toFailure(),
                department = Department.parseDepartment(input.substring(5).take(2))
                    ?: return "Department should be gt 0 and lt 96 or equal to 99".toFailure(),
                city = City.parseCity(input.substring(7).take(3))
                    ?: return "City should be gt 0 and lt 1000".toFailure(),
                serialNumber = SerialNumber.parseSerialNumber(input.substring(10).take(3))
                    ?: return "Serial number should be gt 0 and lt 1000".toFailure()
            )
            return if (nir.checkKey(input)) success(nir) else "".toFailure()
        }

        private fun String.toFailure(): Result<NIR> = failure(ParsingException(this))
    }
}
