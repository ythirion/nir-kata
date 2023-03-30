using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate
{
    public readonly struct City : IEquatable<City>
    {
        private readonly int _value;

        private City(int value) => _value = value;

        public static Option<City> Parse(string potentialCity) =>
            potentialCity
                .ToInt()
                .Filter(x => x is > 0 and <= 999)
                .Match(x => new City(x), Option<City>.None);

        public static explicit operator City(int value) =>
            Parse(value.ToString())
                .Match(y => y, () => throw new InvalidCastException());

        public override string ToString() => _value.ToString("D3");

        public bool Equals(City other) => _value == other._value;

        public override bool Equals(object? obj) => obj is City other && Equals(other);

        public override int GetHashCode() => _value;

        public static bool operator ==(City left, City right) => left.Equals(right);

        public static bool operator !=(City left, City right) => !(left == right);
    }
}