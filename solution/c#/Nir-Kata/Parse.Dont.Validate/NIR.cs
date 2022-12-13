using LanguageExt;
using LanguageExt.UnsafeValueAccess;

namespace Nir_Kata.Parse.Dont.Validate;

public readonly struct NIR
{
    private const int ValidLength = 15;

    private readonly Sex _sex;
    private readonly Year _year;
    private readonly Month _month;
    private readonly Department _department;
    private readonly City _city;
    private readonly SerialNumber _serialNumber;

    public NIR(Sex sex, Year year, Month month, Department department, City city, SerialNumber serialNumber)
    {
        _sex = sex;
        _year = year;
        _month = month;
        _department = department;
        _city = city;
        _serialNumber = serialNumber;
    }

    public static Option<NIR> ParseNIR(string input) =>
        input.Length() == ValidLength
            ? ParseSafely(input)
            : Option<NIR>.None;

    private static bool ValidateKey(NIR nir, int key) => nir.Key() == key;

    public int Key() => (int) (97L - ToStringWithoutKey().ToLong().Value() % 97L);

    private static Option<NIR> ParseSafely(string input) =>
        (from sex in SexParser.Parse(input[0])
            from year in Year.Parse(input[1..3])
            from month in MonthParser.Parse(input[3..5])
            from department in Department.Parse(input[5..7])
            from city in City.Parse(input[7..10])
            from serialNumber in SerialNumber.Parse(input[10..13])
            from key in input[13..15].ToInt()
            select new {NIR = new NIR(sex, year, month, department, city, serialNumber), Key = key})
        .Match(parsedNIR =>
                ValidateKey(parsedNIR.NIR, parsedNIR.Key)
                    ? parsedNIR.NIR
                    : Option<NIR>.None,
            Option<NIR>.None);

    public override string ToString() => ToStringWithoutKey() + Key().ToString("D2");

    private string ToStringWithoutKey() =>
        _sex.ToString("D") + _year + _month.ToIntString() + _department + _city + _serialNumber;
}