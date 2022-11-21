namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module Month =
    type Month = Month of int

    let parse (potentialMonth: string) =
        match Common.parseToInt potentialMonth with
        | Some month when month > 0 && month <= 12 -> Some(Month month)
        | _ -> None

    let apply f (Month month) = f month
    let value s = apply id s
