package parse.dont.validate

import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import parse.dont.validate.NIR.*

object Generators {
    val nirGenerator = arbitrary { _ ->
        NIR(
            sex = Arb.enum<Sex>().bind(),
            year = Year(Arb.int(0, 99).bind()),
            month = Arb.enum<Month>().bind(),
            department = Department(
                Arb.frequency(
                    9 to Arb.int(1, 95),
                    1 to Arb.int(99, 99)
                ).bind()
            ),
            city = City(Arb.int(1, 999).bind()),
            serialNumber = SerialNumber(Arb.int(1, 999).bind())
        )
    }
}