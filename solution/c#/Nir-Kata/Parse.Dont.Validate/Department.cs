using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate
{
    public readonly struct Department : IEquatable<Department>
    {
        private readonly int _value;

        private Department(int value) => _value = value;

        public static explicit operator Department(int value) =>
            Parse(value.ToString())
                .Match(y => y, () => throw new InvalidCastException());

        public static Option<Department> Parse(string potentialDepartment) =>
            potentialDepartment
                .ToInt()
                .Filter(x => x is > 0 and <= 95 or 99)
                .Match(x => new Department(x), Option<Department>.None);

        public override string ToString() => _value.ToString("D2");

        public bool Equals(Department other) => _value == other._value;

        public override bool Equals(object? obj) => obj is Department other && Equals(other);

        public override int GetHashCode() => _value;

        public static bool operator ==(Department left, Department right) => left.Equals(right);

        public static bool operator !=(Department left, Department right) => !(left == right);
    }
}