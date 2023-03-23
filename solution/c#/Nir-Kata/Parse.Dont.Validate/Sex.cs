using LanguageExt;

namespace Nir_Kata.Parse.Dont.Validate
{
    public enum Sex
    {
        M = 1,
        F = 2
    }

    public static class SexParser
    {
        public static Option<Sex> Parse(char potentialSex) =>
            potentialSex switch
            {
                '1' => Sex.M,
                '2' => Sex.F,
                _ => Option<Sex>.None
            };
    }
}