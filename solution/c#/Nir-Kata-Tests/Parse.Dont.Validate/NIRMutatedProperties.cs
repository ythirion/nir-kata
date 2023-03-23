using System.Diagnostics.CodeAnalysis;
using FsCheck;
using FsCheck.Xunit;
using LanguageExt;
using Nir_Kata.Parse.Dont.Validate;
using static System.Tuple;
using static Nir_Kata.Parse.Dont.Validate.NIR;

namespace Nir_Kata_Tests.Parse.Dont.Validate
{
    public class NIRMutatedProperties
    {
        public record Mutator(string Name, Func<NIR, Gen<string>> mutate)
        {
            public string Apply(NIR nir) => mutate(nir).Sample(0, 1).Head;
        }

        private static class MutatorGenerator
        {
            private static Gen<int> digits3Gen =
                Gen.Frequency(Create(7, Gen.Choose(1000, 9999)), Create(3, Gen.Choose(1, 99)));

            private static Mutator sexMutator = new("Sex mutator", nir =>
                Gen.Choose(3, 9)
                    .Select(invalidSex => invalidSex + nir.ToString()[1..15])
            );

            private static Mutator yearMutator = new("Year mutator", nir =>
                Gen.Frequency(Create(7, Gen.Choose(100, 999)), Create(3, Gen.Choose(1, 9)))
                    .Select(invalidYear => $"{nir.ToString()[0]}{invalidYear}{nir.ToString()[3..]}")
            );

            private static Mutator monthMutator = new("Month mutator", nir =>
                Gen.Choose(13, 99)
                    .Select(invalidMonth => $"{nir.ToString()[..3]}{invalidMonth}{nir.ToString()[5..]}")
            );

            private static Mutator departmentMutator = new("Department mutator", nir =>
                Gen.Frequency(Create(7, Gen.Choose(100, 999)), Create(3, Gen.Choose(96, 98)))
                    .Select(invalidDepartment => $"{nir.ToString()[..5]}{invalidDepartment}{nir.ToString()[7..]}")
            );

            private static Mutator cityMutator = new("City mutator", nir =>
                digits3Gen.Select(invalidCity => $"{nir.ToString()[..7]}{invalidCity}{nir.ToString()[10..]}")
            );

            private static Mutator serialNumberMutator = new("Serial Number mutator", nir =>
                digits3Gen
                    .Select(invalidSerialNumber => $"{nir.ToString()[..10]}{invalidSerialNumber}{nir.ToString()[13..]}")
            );

            private static Mutator keyMutator = new("Key mutator", nir =>
                Gen.Choose(0, 97)
                    .Where(x => x != nir.Key())
                    .Select(invalidKey => $"{nir.ToString()[..13]}{invalidKey:D2}")
            );

            private static Mutator truncateMutator = new("Truncate mutator", nir =>
                Gen.Choose(1, 13)
                    .Select(size => $"{nir.ToString()[..size]}")
            );

            [SuppressMessage("ReSharper", "UnusedMember.Local", Justification = "Used by FSCheck")]
            public static Arbitrary<Mutator> Mutator() =>
                Gen.Elements(
                        sexMutator,
                        yearMutator,
                        monthMutator,
                        departmentMutator,
                        cityMutator,
                        serialNumberMutator,
                        keyMutator,
                        truncateMutator
                    )
                    .ToArbitrary();
        }

        [Property(Arbitrary = new[] {typeof(NIRGenerator), typeof(MutatorGenerator)})]
        public Property InvalidNIRCanNeverBeParsed(NIR nir, Mutator mutator) =>
            (ParseNIR(mutator.Apply(nir)) == Option<NIR>.None)
            .ToProperty()
            .Classify(true, mutator.Name);
    }
}