namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module Year =
    type Year = Year of int

    let parse (potentialYear: string) =
        match Common.parseToInt potentialYear with
        | Some year -> Some(Year year)
        | _ -> None

    let apply f (Year year) = f year
    let value s = apply id s
