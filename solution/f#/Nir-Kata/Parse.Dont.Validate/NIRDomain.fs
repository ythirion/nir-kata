namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

open Microsoft.FSharp.Core
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Key

module NIRDomain =
    type ErrorMessage = ErrorMessage of string
    let private toError message = Error(ErrorMessage message)
    let private validNIRLength = 15

    type NIR =
        { sex: Sex.Sex
          year: Year.Year
          month: Month.Month
          department: Department.Department
          city: City.City
          serialNumber: SerialNumber.SerialNumber }
        member private this.toStringWithoutKey: string =
            (Sex.value this.sex).ToString()
            + $"%02i{Year.value this.year}"
            + $"%02i{Month.value this.month}"
            + $"%02i{Department.value this.department}"
            + $"%03i{City.value this.city}"
            + $"%03i{SerialNumber.value this.serialNumber}"

        member this.calculateKey: Option<Key> =
            match this.toStringWithoutKey |> Common.parseToLong with
            | Some n -> 97L - (n % 97L) |> int |> Key |> Some
            | None -> None

        override this.ToString() : string =
            this.toStringWithoutKey
            + (match this.calculateKey with
               | Some key -> $"%02i{Key.value key}"
               | _ -> "")

    let private validateKey (nir: NIR, key: int) = nir.calculateKey = Some(Key key)

    let private toNIR (nir: NIR, key: int) =
        if (nir, key) |> validateKey then
            Ok(nir)
        else
            toError "Invalid key"

    let private parseSafely (input: string) : Result<NIR, ErrorMessage> =
        match Sex.parse input.[0],
              Year.parse input.[1..2],
              Month.parse input.[3..4],
              Department.parse input.[5..6],
              City.parse input.[7..9],
              SerialNumber.parse input.[10..12],
              Common.parseToInt input.[13..14]
            with
        | Some sex, Some year, Some month, Some department, Some city, Some serialNumber, Some key ->
            let nir =
                { sex = sex
                  year = year
                  month = month
                  department = department
                  city = city
                  serialNumber = serialNumber }

            (nir, key) |> toNIR
        | _ -> toError $"Not a valid NIR: {input}"

    let parseNIR (input: string) : Result<NIR, ErrorMessage> =
        if input.Length = validNIRLength then
            parseSafely input
        else
            Error(ErrorMessage $"Not a valid NIR: length of {input.Length} instead of {validNIRLength}")
