namespace Nir_Kata.Parse.Dont.Validate.NIRDomain

open Microsoft.FSharp.Core

module NIRDomain =
    type NIR = { Input: string }
    type ErrorMessage = ErrorMessage of string
    let private validNIRLength = 15

    let private parseSafely input : Result<NIR, ErrorMessage> =
        let nir = { Input = input }
        Ok(nir)

    let parseNIR (input: string) : Result<NIR, ErrorMessage> =
        if input.Length = validNIRLength then
            parseSafely (input)
        else
            Error(ErrorMessage $"Not a valid NIR: should have a length of {input.Length}")
