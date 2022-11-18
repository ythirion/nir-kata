namespace Nir_Kata.Parse.Dont.Validate.NIRDomain


module Department =
    type Department = private Department of int

    let inRange department =
        department > 0 && department <= 96
        || department = 99

    let parse (potentialDepartment: string) =
        match Common.parseToInt potentialDepartment with
        | Some department when inRange (department) -> Some(Department department)
        | _ -> None

    let apply f (Department department) = f department
    let value s = apply id s
