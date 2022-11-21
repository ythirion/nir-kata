namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module Key =
    type Key = Key of int
    let apply f (Key key) = f key
    let value s = apply id s
