using FsCheck;
using FsCheck.Xunit;
using Nir_Kata.Parse.Dont.Validate;
using static Nir_Kata.Parse.Dont.Validate.NIR;

namespace Nir_Kata_Tests.Parse.Dont.Validate
{
    public class NIRProperties
    {
        [Property(Arbitrary = new[] {typeof(NIRGenerator)})]
        public Property RoundTripNIR(NIR nir) =>
            ParseNIR(nir.ToString())
                .Contains(nir)
                .ToProperty();
    }
}