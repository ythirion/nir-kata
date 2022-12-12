using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate;

public readonly struct NIR
{
    private const int ValidLength = 15;

    private readonly Sex _sex;
    private readonly Year _year;

    public NIR(Sex sex, Year year)
    {
        _sex = sex;
        _year = year;
    }

    public static Option<NIR> ParseNIR(string input) =>
        input.Length() == ValidLength
            ? ParseSafely(input)
            : Option<NIR>.None;

    private static Option<NIR> ParseSafely(string input) =>
        from sex in SexParser.ParseSex(input[0])
        from year in Year.Parse(input.Substring(1, 2))
        select new NIR(sex, year);

    public override string ToString() =>
        _sex.ToString("D") + _year;
}