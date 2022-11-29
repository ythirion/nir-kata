package parse.dont.validate

data class ParsingException(override val message: String) : Exception(message)