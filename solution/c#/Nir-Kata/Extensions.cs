using LanguageExt;

namespace Nir_Kata
{
    public static class Extensions
    {
        public static Option<int> ToInt(this string potentialNumber) =>
            int.TryParse(potentialNumber, out var number)
                ? number
                : Option<int>.None;

        public static Option<long> ToLong(this string potentialNumber) =>
            long.TryParse(potentialNumber, out var number)
                ? number
                : Option<long>.None;

        public static bool IsANumber(this string str) => str.All(char.IsDigit);
    }
}