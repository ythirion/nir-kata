package parse.dont.validate;

import io.vavr.Tuple;
import io.vavr.test.Arbitrary;
import io.vavr.test.Gen;

import java.util.Random;

public class NIRGenerator {
    private static final Random random = new Random();
    private static final Gen<Sex> sexGenerator = Gen.choose(Sex.values());
    private static final Gen<Year> yearGenerator = Gen.choose(0, 99).map(Year::fromInt);
    private static final Gen<Month> monthGenerator = Gen.choose(Month.values());
    private static final Gen<Department> departmentGenerator =
            Gen.frequency(Tuple.of(9, Gen.choose(1, 95)),
                            Tuple.of(1, Gen.choose(99)))
                    .map(Department::fromInt);

    private static final Gen<City> cityGenerator = Gen.choose(1, 999).map(City::fromInt);
    private static final Gen<SerialNumber> serialNumberGenerator = Gen.choose(1, 999).map(SerialNumber::fromInt);
    public static final Arbitrary<NIR> validNIR =
            sexGenerator
                    .map(NIRBuilder::new)
                    .map(nirBuilder -> nirBuilder.withYear(yearGenerator.apply(random)))
                    .map(nirBuilder -> nirBuilder.withMonth(monthGenerator.apply(random)))
                    .map(nirBuilder -> nirBuilder.withDepartment(departmentGenerator.apply(random)))
                    .map(nirBuilder -> nirBuilder.withCity(cityGenerator.apply(random)))
                    .map(nirBuilder -> nirBuilder.withSerialNumber(serialNumberGenerator.apply(random)))
                    .map(x -> new NIR(
                                    x.getSex(),
                                    x.getYear(),
                                    x.getMonth(),
                                    x.getDepartment(),
                                    x.getCity(),
                                    x.getSerialNumber()
                            )
                    )
                    .arbitrary();
}
