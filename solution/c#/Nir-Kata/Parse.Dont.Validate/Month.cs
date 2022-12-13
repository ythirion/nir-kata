using LanguageExt;
using static LanguageExt.Option<Nir_Kata.Parse.Dont.Validate.Month>;

namespace Nir_Kata.Parse.Dont.Validate;

public enum Month
{
    Jan = 1,
    Feb = 2,
    Mar = 3,
    Apr = 4,
    May = 5,
    Jun = 6,
    Jul = 7,
    Aou = 8,
    Sep = 9,
    Oct = 10,
    Nov = 11,
    Dec = 12
}

public static class MonthParser
{
    public static Option<Month> Parse(string potentialMonth) =>
        potentialMonth
            .ToInt()
            .Bind(FromInt)
            .Match(month => month, None);

    private static Option<Month> FromInt(int x) =>
        Enum.GetValues<Month>()
            .Find(m => (int) m == x)
            .Map(foundMonth => foundMonth);
}

public static class MonthExtensions
{
    public static string ToIntString(this Month month) => ((int) month).ToString("D2");
}