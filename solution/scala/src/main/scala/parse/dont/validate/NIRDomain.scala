package parse.dont.validate

import parse.dont.validate.NIRDomain.City.parseCity
import parse.dont.validate.NIRDomain.Department.parseDepartment
import parse.dont.validate.NIRDomain.Month.parseMonth
import parse.dont.validate.NIRDomain.SerialNumber.parseSerialNumber
import parse.dont.validate.NIRDomain.Sex.*
import parse.dont.validate.NIRDomain.Year.parseYear
import parse.dont.validate.NIRDomain.{ParsingError, Sex, Year}

import scala.language.strictEquality

object NIRDomain {
  class NIR(
      val sex: Sex,
      val year: Year,
      val month: Month,
      val department: Department,
      val city: City,
      val serialNumber: SerialNumber
  ) {
    override def toString: String =
      stringWithoutKey() + f"${calculateKey()}%02d"

    override def equals(other: Any): Boolean = other match
      case otherNir: NIR =>
        (sex equals otherNir.sex)
        && year == otherNir.year
        && (month equals otherNir.month)
        && department == otherNir.department
        && city == otherNir.city
        && serialNumber == otherNir.serialNumber
      case _ => false

    private def stringWithoutKey(): String =
      sexToString
        + f"$year%02d"
        + f"${month.ordinal + 1}%02d"
        + f"$department%02d"
        + f"$city%03d"
        + f"$serialNumber%03d"

    private def sexToString: String = if (sex equals M) "1" else "2"

    def calculateKey(): Key = Key((97 - (stringWithoutKey().toLong % 97)).toInt)
  }

  object NIR {
    private val length = 15

    def parseNIR(input: String): Either[ParsingError, NIR] = {
      if (input.length != length)
        return Left(s"Not a valid NIR: should have a length of $length")

      parseSafely(input)
        .flatMap(nir => checkKey(input.substring(13), nir))
    }

    private def parseSafely(input: String): Either[ParsingError, NIR] = {
      for {
        sex <- parseSex(input.charAt(0))
        year <- parseYear(input.substring(1).take(2))
        month <- parseMonth(input.substring(3).take(2))
        department <- parseDepartment(input.substring(5).take(2))
        city <- parseCity(input.substring(7).take(3))
        serialNumber <- parseSerialNumber(input.substring(10).take(3))
      } yield NIR(sex, year, month, department, city, serialNumber)
    }

    private def checkKey(input: String, nir: NIR): Either[ParsingError, NIR] =
      readInt(input) match {
        case Right(key) if key == nir.calculateKey() => Right(nir)
        case _ => Left("Invalid key")
      }
  }

  opaque type ParsingError = String

  object ParsingError:
    def apply(error: String): ParsingError = error

  opaque type Year = Int

  object Year {
    def apply(year: Int): Year = rightOrFail(
      parseYear(year.toString)
    )

    def parseYear(input: String): Either[ParsingError, Year] =
      readInt(input) match {
        case Right(x) if x >= 0 && x < 100 => Right(x)
        case _ => Left("year should be between 0 and 99")
      }
  }

  enum Sex:
    case M, F

  object Sex {
    def parseSex(input: Char): Either[ParsingError, Sex] =
      input match {
        case '1' => Right(M)
        case '2' => Right(F)
        case _ => Left("Not a valid sex")
      }
  }

  enum Month:
    case Jan, Feb, Mar, Apr, May, Jun, Jul, Aou, Sep, Oct, Nov, Dec

  object Month {
    def parseMonth(input: String): Either[ParsingError, Month] =
      readInt(input) match {
        case Right(m) if m > 0 && m <= 12 => Right(Month.fromOrdinal(m - 1))
        case _ => Left("Not a valid month")
      }
  }

  opaque type Department = Int

  object Department {
    private val otherCountry: Int = 99

    def apply(department: Int): Department = rightOrFail(
      parseDepartment(department.toString)
    )

    def parseDepartment(input: String): Either[ParsingError, Department] =
      readInt(input) match {
        case Right(x) if x > 0 && x < 96 => Right(x)
        case Right(x) if x == 99 => Right(otherCountry)
        case _ => Left("department should be gt 0 and lt 96 or equal to 99")
      }
  }

  opaque type City = Int

  object City {
    def apply(city: Int): City = rightOrFail(
      parseCity(city.toString)
    )

    def parseCity(input: String): Either[ParsingError, City] =
      readInt(input) match {
        case Right(x) if x > 0 && x < 1000 => Right(x)
        case _ => Left("city should be gt 0 and lt 1000")
      }
  }

  opaque type SerialNumber = Int

  object SerialNumber {
    def apply(serialNumber: Int): SerialNumber = rightOrFail(
      parseSerialNumber(serialNumber.toString)
    )

    def parseSerialNumber(input: String): Either[ParsingError, SerialNumber] =
      readInt(input) match {
        case Right(x) if x > 0 && x < 1000 => Right(x)
        case _ => Left("serial number should be gt 0 and lt 1000")
      }
  }

  opaque type Key = Int

  object Key {
    def apply(key: Int): Key =
      if (key < 0 || key > 99) failure("invalid key")
      else key

    extension (key: Key) {
      def value: Int = key
    }
  }

  private def rightOrFail[T](either: Either[ParsingError, T]): T =
    either match {
      case Right(x) => x
      case Left(error) => failure(error)
    }

  private def failure(invalidMessage: String) =
    throw new IllegalArgumentException(invalidMessage)

  private def readInt(input: String): Either[ParsingError, Int] =
    input.toIntOption match {
      case Some(x) => Right(x)
      case None => Left("Not a valid number")
    }
}
