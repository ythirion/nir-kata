using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate
{
    public readonly struct SerialNumber : IEquatable<SerialNumber>
    {
        private readonly int _value;

        private SerialNumber(int value) => _value = value;

        public static explicit operator SerialNumber(int value) =>
            Parse(value.ToString())
                .Match(y => y, () => throw new InvalidCastException());

        public static Option<SerialNumber> Parse(string potentialDepartment) =>
            potentialDepartment
                .ToInt()
                .Filter(x => x is > 0 and <= 999)
                .Match(x => new SerialNumber(x), Option<SerialNumber>.None);

        public override string ToString() => _value.ToString("D3");

        public bool Equals(SerialNumber other) => _value == other._value;

        public override bool Equals(object? obj) => obj is SerialNumber other && Equals(other);

        public override int GetHashCode() => _value;

        public static bool operator ==(SerialNumber left, SerialNumber right) => left.Equals(right);

        public static bool operator !=(SerialNumber left, SerialNumber right) => !(left == right);
    }
}