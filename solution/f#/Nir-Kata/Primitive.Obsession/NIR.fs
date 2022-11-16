namespace Nir_Kata.Primitive.Obsession

open System

module NIR =
    let private validNIRLength = 15
    let private isANumber str = str |> Seq.forall Char.IsDigit

    let private readNumber str : Option<int64> =
        if isANumber str then
            Some(str |> int64)
        else
            None

    let private validateLength (input: string, continueWith: string -> bool) =
        if input.Length = validNIRLength then
            continueWith input
        else
            false

    let private validateSex sex = sex = '1' || sex = '2'
    let private validateYear year = isANumber year

    let private validateNumber (str: string, rule: int64 -> bool) =
        match readNumber str with
        | Some n -> rule n
        | _ -> false

    let private validateMonth month =
        validateNumber (month, (fun x -> x > 0 && x <= 12))

    let private validateDepartment department =
        validateNumber (department, (fun m -> (m > 0 && m <= 95) || m = 99))

    let private validateCity city = isANumber city
    let private validateSerialNumber serialNumber = isANumber serialNumber
    let private calculateKey number : int64 = 97L - (number % 97L)

    let private validateKey number key =
        match (readNumber number, readNumber key) with
        | Some n, Some k -> calculateKey n = k
        | _ -> false

    let private validateCharacters (input: string) =
        validateSex input.[0]
        && validateYear input.[1..2]
        && validateMonth input.[3..4]
        && validateDepartment input.[5..6]
        && validateCity input.[7..9]
        && validateSerialNumber input.[10..12]
        && validateKey input.[0..12] input.[13..14]

    let validate input =
        validateLength (input, validateCharacters)
