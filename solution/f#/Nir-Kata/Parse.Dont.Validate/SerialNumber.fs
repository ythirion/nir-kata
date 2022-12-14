namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module SerialNumber =
    type SerialNumber = SerialNumber of int

    let parse (potentialSerialNumber: string) =
        match Common.parseToInt potentialSerialNumber with
        | Some number when number >=1 && number <= 999 -> Some(SerialNumber number)
        | _ -> None

    let apply f (SerialNumber number) = f number
    let value s = apply id s
