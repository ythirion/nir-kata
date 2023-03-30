using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate
{
    public readonly struct Year : IEquatable<Year>
    {
        private readonly int _value;

        private Year(int value) => _value = value;

        public static explicit operator Year(int value) =>
            Parse(value.ToString())
                .Match(y => y, () => throw new InvalidCastException());

        public static Option<Year> Parse(string potentialYear) =>
            potentialYear
                .ToInt()
                .Filter(x => x is >= 0 and <= 99)
                .Match(x => new Year(x), Option<Year>.None);

        public override string ToString() => _value.ToString("D2");

        public bool Equals(Year other) => _value == other._value;

        public override bool Equals(object? obj) => obj is Year other && Equals(other);

        public override int GetHashCode() => _value;

        public static bool operator ==(Year left, Year right) => left.Equals(right);

        public static bool operator !=(Year left, Year right) => !(left == right);
    }
}