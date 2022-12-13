using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate;

public readonly struct Department
{
    private readonly int _value;

    private Department(int value) => _value = value;

    public static explicit operator Department(int value) =>
        Parse(value.ToString())
            .Match(y => y, () => throw new InvalidCastException());

    public static Option<Department> Parse(string potentialDepartment) =>
        potentialDepartment
            .ToInt()
            .Match(x => new Department(x), Option<Department>.None);

    public override string ToString() => _value.ToString("D2");
}