namespace Nir_Kata.Primitive.Obsession
{
    public static class NIR
    {
        private const int ValidLength = 15;

        public static bool Validate(string input) => ValidateLength(input)
                                                     && ValidateSex(input[0])
                                                     && ValidateYear(input[1..3])
                                                     && ValidateMonth(input[3..5])
                                                     && ValidateDepartment(input[5..7])
                                                     && ValidateCity(input[7..10])
                                                     && ValidateSerialNumber(input[10..13])
                                                     && ValidateKey(input[..13], input[13..15]);

        private static bool ValidateLength(string input) => input.Length == ValidLength;

        private static bool ValidateSex(char sex) => sex is '1' or '2';
        private static bool ValidateYear(string year) => year.IsANumber();

        private static bool ValidateMonth(string month) =>
            ValidateNumber(month, m => m is > 0 and <= 12);

        private static bool ValidateDepartment(string department) =>
            ValidateNumber(department, d => d is > 0 and <= 95 or 99);

        private static bool ValidateCity(string city) => city.IsANumber();

        private static bool ValidateSerialNumber(string serialNumber) => serialNumber.IsANumber();

        private static bool ValidateNumber(string potentialNumber, Predicate<int> predicate) =>
            potentialNumber
                .ToInt()
                .Match(number => predicate(number), false);

        private static bool ValidateKey(string number, string key) =>
            number
                .ToLong()
                .Match(n => IsValidKey(n, key), false);

        private static bool IsValidKey(long number, string key) =>
            key
                .ToInt()
                .Match(k => CalculateKey(number) == k, false);

        private static long CalculateKey(long number) => 97 - (number % 97);
    }
}