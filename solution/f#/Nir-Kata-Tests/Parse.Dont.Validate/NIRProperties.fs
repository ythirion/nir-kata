module NIRProperties

open FsCheck.Xunit
open NirGenerator
open Nir_Kata.Parse.Dont.Validate.NIRDomain.NIRDomain

let private shouldBeOk nir  =
    function
    | Ok parsedNir -> nir = parsedNir
    | _ -> failwith "Should be OK"

[<Property(Arbitrary = [| typeof<NirGenerator> |])>]
let ``roundtrip nir`` (nir: NIR) =
    nir.ToString()
    |> parseNIR
    |> shouldBeOk nir