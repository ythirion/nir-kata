package parse.dont.validate

import parse.dont.validate.NIR.Sex.Parser.parseSex
import parse.dont.validate.NIR.Year.Parser.parseYear
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

fun NIR.Sex.toInt(): Int = if (this == NIR.Sex.M) 1 else 2

data class NIR(
    val sex: Sex,
    val year: Year
) {
    override fun toString(): String = toLong().toString() + calculateKey()
    private fun toLong(): Long = (sex.toInt().toString() + year).toLong()
    private fun calculateKey(): Long = 97 - (toLong() % 97)

    enum class Sex {
        M, F;

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
    value class Year(private val input: Long) {
        init {
            require(input.isValid())
        }

        override fun toString(): String = String.format("%02d", input)

        companion object Parser {
            private fun Long.isValid() = this in 0..100

            fun parseYear(input: String): Year? =
                when (input.toLongOrNull()?.isValid()) {
                    true -> Year(input.toLong())
                    else -> null
                }
        }
    }

    companion object Parser {
        private const val validLength = 15

        fun parseNIR(input: String): Result<NIR> =
            if (input.length != validLength) failure(ParsingException("Not a valid NIR: should have a length of $validLength"))
            else parseSafely(input)

        private fun parseSafely(input: String): Result<NIR> {
            return success(
                NIR(
                    sex = parseSex(input[0]) ?: return failure(ParsingException("Not a valid sex")),
                    year = parseYear(input.substring(1).take(2))
                        ?: return failure(ParsingException("Year should be positive and lt 100"))
                )
            )
        }
    }
}
