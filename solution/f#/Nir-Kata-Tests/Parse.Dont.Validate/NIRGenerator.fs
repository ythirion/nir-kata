module NirGenerator

open FsCheck
open Nir_Kata.Parse.Dont.Validate.NIRDomain.City
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Department
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Month
open Nir_Kata.Parse.Dont.Validate.NIRDomain.NIRDomain
open Nir_Kata.Parse.Dont.Validate.NIRDomain.SerialNumber
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Sex
open Nir_Kata.Parse.Dont.Validate.NIRDomain.Year

type NirGenerator =
    static member Nir() : Arbitrary<NIR> =
        gen {
            let! sex = Arb.generate<Sex>
            let! year = Gen.choose (0, 99)
            let! month = Gen.choose (1, 12)

            let! department =
                Gen.frequency [ (9, Gen.choose (1, 95))
                                (1, Gen.constant 99) ]

            let! city = Gen.choose (1, 999)
            let! serialNumber = Gen.choose (1, 999)

            return
                { sex = sex
                  year = Year year
                  month = Month month
                  department = Department department
                  city = City city
                  serialNumber = SerialNumber serialNumber }
        }
        |> Arb.fromGen
