package primitive.obsession

object NIR {
  private val length = 15
  type NIR = String

  def validate(input: NIR): Boolean = {
    validateLength(input) { validInput =>
      validateSex(validInput.charAt(0)) &&
      validateYear(validInput.substring(1).take(2)) &&
      validateMonth(validInput.substring(3).take(2)) &&
      validateDepartment(validInput.substring(5).take(2)) &&
      validateCity(validInput.substring(7).take(3)) &&
      validateSerialNumber(validInput.substring(10).take(3)) &&
      validateKey(validInput.substring(0, 13), validInput.substring(13))
    }
  }

  private def validateLength(
      input: String
  )(continueWith: String => Boolean): Boolean =
    if (input.length == length) continueWith(input)
    else false

  private def validateSex(sex: Char): Boolean = sex == '1' || sex == '2'

  private def validateYear(year: String): Boolean = isANumber(year)

  private def validateMonth(month: String): Boolean =
    validateNumber(month, m => m > 0 && m <= 12)

  private def validateDepartment(department: String): Boolean =
    validateNumber(department, m => (m > 0 && m <= 95) || m == 99)

  private def validateCity(city: String): Boolean = isANumber(city)

  private def validateSerialNumber(serialNumber: String): Boolean =
    isANumber(serialNumber)

  private def validateNumber(str: String, predicate: Long => Boolean): Boolean =
    readNumber(str).exists(predicate)

  private def readNumber(str: String): Option[Long] = str.toLongOption

  private def validateKey(number: String, key: String): Boolean =
    (readNumber(number), readNumber(key)) match {
      case (Some(n), Some(k)) => calculateKey(n) == k
      case _                  => false
    }

  private def calculateKey(number: Long): Long = 97 - (number % 97)
  private def isANumber(str: String) = str.forall(c => c.isDigit)
}
