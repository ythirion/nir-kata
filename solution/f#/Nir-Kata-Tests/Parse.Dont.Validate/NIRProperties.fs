module NIRProperties

open System
open FsCheck.Xunit
open Nir_Kata.Parse.Dont.Validate.NIRDomain.NIRDomain

[<Property>]
let ``roundtrip nir`` (nir: NIR) =
    match parseNIR (nir.ToString()) with
    | Ok roundTrippedNIR -> roundTrippedNIR = nir
    | Error error -> raise (Exception(error.ToString()))
