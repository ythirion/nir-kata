using System.Diagnostics.CodeAnalysis;
using FluentAssertions.LanguageExt;
using FsCheck;
using FsCheck.Xunit;
using Nir_Kata.Parse.Dont.Validate;

namespace Nir_Kata_Tests.Parse.Dont.Validate;

public class NIRMutatedProperties
{
    public record Mutator(string Name, Func<NIR, Gen<string>> mutate)
    {
        public string Apply(NIR nir) => mutate(nir).Sample(0, 1).Head;
    }

    private static class MutatorGenerator
    {
        private static Mutator sexMutator = new("Sex mutator", nir =>
            Gen.Choose(3, 9)
                .Select(invalidSex => invalidSex + nir.ToString()[1..15])
        );

        private static Mutator yearMutator = new("Year mutator", nir =>
            Gen.Frequency(Tuple.Create(7, Gen.Choose(100, 999)), Tuple.Create(3, Gen.Choose(1, 9)))
                .Select(invalidYear => $"{nir.ToString()[0]}{invalidYear}{nir.ToString()[3..]}")
        );

        private static Mutator monthMutator = new("Month mutator", nir =>
            Gen.Choose(13, 99)
                .Select(invalidMonth => $"{nir.ToString()[..3]}{invalidMonth}{nir.ToString()[5..]}")
        );


        [SuppressMessage("ReSharper", "UnusedMember.Local", Justification = "Used by FSCheck")]
        public static Arbitrary<Mutator> Mutator() =>
            Gen.Elements(
                    sexMutator,
                    yearMutator,
                    monthMutator
                )
                .ToArbitrary();
    }

    [Property(Arbitrary = new[] {typeof(NIRGenerator), typeof(MutatorGenerator)})]
    public void InvalidNIRCanNeverBeParsed(NIR nir, Mutator mutator) =>
        NIR.ParseNIR(mutator.Apply(nir))
            .Should()
            .BeNone();
}