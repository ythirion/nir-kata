namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

module Sex =
    type Sex =
        private
        | M
        | F

    let parse (potentialSex: char) =
        match potentialSex with
        | '1' -> Some M
        | '2' -> Some F
        | _ -> None

    let value s = if (s = M) then 1 else 2
