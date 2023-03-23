using System.Diagnostics.CodeAnalysis;
using FsCheck;
using Nir_Kata.Parse.Dont.Validate;
using static System.Tuple;

namespace Nir_Kata_Tests.Parse.Dont.Validate
{
    public static class NIRGenerator
    {
        [SuppressMessage("ReSharper", "UnusedMember.Local", Justification = "Used by FSCheck")]
        public static Arbitrary<NIR> Nir() =>
            (from sex in Arb.Generate<Sex>()
                from year in Gen.Choose(0, 99)
                from month in Arb.Generate<Month>()
                from department in Gen.Frequency(new[] {Create(9, Gen.Choose(1, 95)), Create(1, Gen.Constant(99))})
                from city in Gen.Choose(1, 999)
                from serialNumber in Gen.Choose(1, 999)
                select new NIR(sex, (Year) year, month, (Department) department, (City) city,
                    (SerialNumber) serialNumber)
            ).ToArbitrary();
    }
}