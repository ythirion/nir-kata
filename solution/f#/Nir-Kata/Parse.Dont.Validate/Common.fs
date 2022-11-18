namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

open System

module Common =
    let parseToInt (potentialInt: string) =
        try
            potentialInt |> int |> Some
        with
        | :? FormatException -> None

    let parseToLong (potentialLong: string) =
        try
            potentialLong |> int64 |> Some
        with
        | :? FormatException -> None
