module NIRMutatedProperties

open FsCheck
open FsCheck.Xunit
open Microsoft.FSharp.Core
open NirGenerator
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Key
open Nir_Kata.Parse.Dont.Validate.NIRDomain.NIRDomain

type Mutator =
    { name: string
      func: NIR -> Gen<string> }

let private digits3Gen =
    Gen.frequency [ (7, Gen.choose (1000, 9999))
                    (3, Gen.choose (1, 99)) ]

let private sexMutator: Mutator =
    { name = "Sex mutator"
      func =
        fun nir ->
            Gen.choose (3, 9)
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidSex -> invalidSex + nir.ToString().[1..]) }

let private yearMutator: Mutator =
    { name = "Year mutator"
      func =
        fun nir ->
            Gen.frequency [ (7, Gen.choose (100, 999))
                            (3, Gen.choose (1, 9)) ]
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidYear -> $"{nir.ToString().[0]}{invalidYear}{nir.ToString().[3..]}") }

let private monthMutator: Mutator =
    { name = "Month mutator"
      func =
        fun nir ->
            Gen.choose (13, 99)
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidMonth -> $"{nir.ToString().[0..2]}{invalidMonth}{nir.ToString().[5..]}") }

let private departmentMutator: Mutator =
    { name = "Department mutator"
      func =
        fun nir ->
            Gen.frequency [ (7, Gen.choose (100, 999))
                            (3, Gen.choose (96, 98)) ]
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidDepartment -> $"{nir.ToString().[0..4]}{invalidDepartment}{nir.ToString().[7..]}") }

let private cityMutator: Mutator =
    { name = "City mutator"
      func =
        fun nir ->
            digits3Gen
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidCity -> $"{nir.ToString().[0..6]}{invalidCity}{nir.ToString().[10..]}") }

let private serialNumberMutator: Mutator =
    { name = "Serial Number mutator"
      func =
        fun nir ->
            digits3Gen
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidSerialNumber ->
                $"{nir.ToString().[0..9]}{invalidSerialNumber}{nir.ToString().[13..]}") }

let private keyMutator: Mutator =
    { name = "Key mutator"
      func =
        fun nir ->
            Gen.choose (0, 97)
            |> Gen.filter (fun x -> Key x <> nir.calculateKey.Value)
            |> Gen.map (fun s -> s |> string)
            |> Gen.map (fun invalidKey -> $"{nir.ToString().[0..12]}{invalidKey}") }

let private truncateMutator: Mutator =
    { name = "Truncate mutator"
      func =
        fun nir ->
            Gen.choose (1, 13)
            |> Gen.map (fun size -> nir.ToString().[0..size]) }

type MutatorGenerator =
    static member mutate() : Arbitrary<Mutator> =
        Gen.elements [ sexMutator
                       yearMutator
                       monthMutator
                       departmentMutator
                       cityMutator
                       serialNumberMutator
                       keyMutator
                       truncateMutator ]
        |> Arb.fromGen

let private shouldBeError =
    function
    | Error _ -> ()
    | _ -> failwith "Should not be OK"

[<Property(Arbitrary = [| typeof<NirGenerator>
                          typeof<MutatorGenerator> |])>]
let ``invalid nir can never be parsed`` (nir: NIR, mutator: Mutator) =
    mutator.func nir
    |> Gen.sample 0 1
    |> List.head
    |> parseNIR
    |> shouldBeError
    |> Prop.classify true mutator.name
