namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

open System
open Microsoft.FSharp.Core

module NIRDomain =
    type ErrorMessage = ErrorMessage of string
    let private toError message = Error(ErrorMessage message)
    let private validNIRLength = 15
    let private isANumber str = str |> Seq.forall Char.IsDigit

    type Sex =
        private
        | M
        | F

    type NIR =
        private
            { sex: Sex
              year: Year.Year
              month: Month.Month }

    let private parseSex input : Option<Sex> =
        match input with
        | '1' -> Some M
        | '2' -> Some F
        | _ -> None

    let private parseSafely (input: string) : Result<NIR, ErrorMessage> =
        match parseSex input.[0], Year.parse input.[1..2], Month.parse input.[3..4] with
        | Some sex, Some year, Some month ->
            Ok(
                { sex = sex
                  year = year
                  month = month }
            )
        | _ -> toError $"Unable to parse {input}"

    let parseNIR (input: string) : Result<NIR, ErrorMessage> =
        if input.Length = validNIRLength then
            parseSafely input
        else
            Error(ErrorMessage $"Not a valid NIR: length of {input.Length} instead of {validNIRLength}")
