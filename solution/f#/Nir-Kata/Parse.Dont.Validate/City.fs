namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module City =
    type City = City of int

    let parse (potentialCity: string) =
        match Common.parseToInt potentialCity with
        | Some city when city >= 1 && city <= 999 -> Some(City city)
        | _ -> None

    let apply f (City city) = f city
    let value s = apply id s
