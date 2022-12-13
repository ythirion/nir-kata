using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate;

public readonly struct NIR
{
    private const int ValidLength = 15;

    private readonly Sex _sex;
    private readonly Year _year;
    private readonly Month _month;

    public NIR(Sex sex, Year year, Month month)
    {
        _sex = sex;
        _year = year;
        _month = month;
    }

    public static Option<NIR> ParseNIR(string input) =>
        input.Length() == ValidLength
            ? ParseSafely(input)
            : Option<NIR>.None;

    private static Option<NIR> ParseSafely(string input) =>
        from sex in SexParser.Parse(input[0])
        from year in Year.Parse(input.Substring(1, 2))
        from month in MonthParser.Parse(input.Substring(3, 2))
        select new NIR(sex, year, month);

    public override string ToString() =>
        _sex.ToString("D") + _year + _month.ToString("D");
}