package primitive.obsession

object NIR {
    private const val validLength = 15
    private fun String.isANumber() = this.all { it.isDigit() }
    private fun String.validateNumber(isValid: (Long) -> Boolean): Boolean =
        this.toLongOrNull()?.let { isValid(it) } == true

    fun validateNIR(input: String): Boolean {
        return validateLength(input) {
            validateSex(it[0]) &&
                    validateYear(it.substring(1).take(2)) &&
                    validateMonth(it.substring(3).take(2)) &&
                    validateDepartment(it.substring(5).take(2)) &&
                    validateCity(it.substring(7).take(3)) &&
                    validateSerialNumber(it.substring(10).take(3)) &&
                    validateKey(it.substring(0, 13), it.substring(13))
        }
    }

    private fun validateLength(input: String, continueWith: (String) -> Boolean): Boolean {
        if (input.length != validLength) return false
        return continueWith(input)
    }

    private fun validateSex(sex: Char) = sex == '1' || sex == '2'
    private fun validateYear(year: String): Boolean = year.isANumber()
    private fun validateMonth(month: String): Boolean = month.validateNumber { it in 1..12 }
    private fun validateDepartment(department: String): Boolean = department.validateNumber { it in 1..95 || it == 99L }
    private fun validateCity(city: String): Boolean = city.isANumber()
    private fun validateSerialNumber(serialNumber: String): Boolean = serialNumber.isANumber()
    private fun calculateKey(number: Long): Long = 97 - (number % 97)
    private fun validateKey(number: String, key: String): Boolean =
        number.toLongOrNull()?.let { calculateKey(it) } == key.toLongOrNull()
}