namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

open Microsoft.FSharp.Core

module NIRDomain =
    type ErrorMessage = ErrorMessage of string
    let private toError message = Error(ErrorMessage message)
    let private validNIRLength = 15

    type Sex =
        private
        | M
        | F

    type Key = private Key of int

    type NIR =
        private
            { sex: Sex
              year: Year.Year
              month: Month.Month
              department: Department.Department
              city: City.City
              serialNumber: SerialNumber.SerialNumber }
        override this.ToString() : string =
            $"{this.sex}{this.year}{this.month}{this.department}{this.city}{this.serialNumber}"

    let private calculateKey (nir: NIR) : Option<Key> =
        match Common.parseToLong (nir.ToString()) with
        | Some n -> 97L - (n % 97L) |> int |> Key |> Some
        | None -> None

    let private parseSex input : Option<Sex> =
        match input with
        | '1' -> Some M
        | '2' -> Some F
        | _ -> None

    let private validateKey (nir: NIR, key: int) = calculateKey nir = Some(Key key)

    let private parseSafely (input: string) : Result<NIR, ErrorMessage> =
        match parseSex input.[0],
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

            if (validateKey (nir, key)) then
                Ok(nir)
            else
                toError $"Invalid key for: {input}"

        | _ -> toError $"Not a valid NIR: {input}"

    let parseNIR (input: string) : Result<NIR, ErrorMessage> =
        if input.Length = validNIRLength then
            parseSafely input
        else
            Error(ErrorMessage $"Not a valid NIR: length of {input.Length} instead of {validNIRLength}")
