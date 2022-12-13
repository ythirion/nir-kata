using FsCheck;
using Nir_Kata.Parse.Dont.Validate;

namespace Nir_Kata_Tests.Parse.Dont.Validate;

public static class NIRGenerator
{
    public static Arbitrary<NIR> Nir() =>
        (from sex in Arb.Generate<Sex>()
            from year in Gen.Choose(0, 99)
            from month in Arb.Generate<Month>()
            select new NIR(sex, (Year) year, month)
        ).ToArbitrary();
}