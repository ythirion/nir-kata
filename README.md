# NIR kata
- Write a system that can handle `NIR` (simplified rules explained below)
- You must apply ["parse don't validate"](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/) principle
- Your `parsing function` must respect the below property
```text
for all (validNir)
parseNIR(nir.toString) == nir
```

in other words with `scalacheck`:

```scala
property("isomorphic") = forAll(validNIR) { nir =>
    parseNIR(nir.toString).value == nir
  }
```

With `parse don't validate` we want to make it impossible to represent an invalid `NIR` in our system:

- You can use `scala 3` [opaque types](https://docs.scala-lang.org/scala3/book/types-opaque-types.html)
- Use "Property-Based Testing" with `scalacheck` to drive your implementation

Your parser may look like this: `String -> Either[ParsingError, NIR]`

## How to
- Start with a `parser` that always returns `Right[NIR]`
  - Write a minimalist data structure first (empty one)
- Write a `positive property` test checking valid NIR can be round-tripped
  - Round-tripping: `NIR -> String -> NIR`
    - Assert that round-tripped `NIR` equals original `NIR` 
  - To do so, you will have to create your own valid NIR generator
- Write a `negative property` test checking `invalid NIRs` can not be parsed
  - This is where mutations are introduced
  - Each different mutation type representing some possible alteration of the `NIR`
  - Generate invalid NIRs by introducing mutations in the valid ones
- Use the properties to guide your implementation

Inspired by [Arnaud Bailly](https://abailly.github.io/about.html)

## NIR rules
`NIR` stands for "Numéro de sécurité sociale en France" it is a unique id representing an individual composed by 15 characters.

Here are the simplified specifications you will use for this kata:

| Positions  | Meaning                                                                                         | Possible values                     |
|------------|-------------------------------------------------------------------------------------------------|-------------------------------------|
| 1          | Sex : 1 for men, 2 for women                                                                    | 1 or 2                              |
| 2, 3       | Last two digits of the year of birth (which gives the year to the nearest century)              | From 00 to 99                       |
| 4, 5       | Birth month                                                                                     | From 01 (January) to 12 (December)  |
| 6, 7       | Department of birth                                                                             | From 01 to 95, 99 for births abroad |
| 8, 9, 10   | Official code of the city of birth                                                              | From 001 to 999                     |
| 11, 12, 13 | "Serial number": birth order number in the month and city                                       | From 001 to 999                     |
| 14, 15     | control key = complement to 97 of the number formed by the first 13 digits of the NIR modulo 97 | From 01 to 97                       |

![nir example](img/nir.jpg)

### Examples
Here are some `valid NIRs` regarding those specifications:
- 254031088723464
- 195017262676215
- 155053933981739
- 106099955391094
- 221035671987783
- 171108279305362

And here are some `invalid` ones:
- 242106259
- 23401289555186
- 257079684484218
- 29602874966603
- 2001082067222817
- 279101327853189
- 159032201199947
- 928086837619542
- 1960487994797875

## Bulletproof your code with "Mutation-based Property-Driven Development"
Once implemented, you can challenge your system by introducing some mutants in your code.
We can create mutation on purpose thanks to our strong typing system (by introducing mutants in `valid NIRs`).

Some example of mutations:
- `Sex` mutant: a value greater than 2 for example
- `Truncate` mutant: truncate some characters in the nir string
- `Key` mutant: change the key by using a number between 1 and 97 that does not respect the key definition  

![Mutation-based Property-Driven Development](img/mutation-based-property-driven-development.png)

Read more about it [here](https://abailly.github.io/posts/mutation-testing.html)

## "Solution"
Proposal of solution are available in the `solution` directory:

- `scala 3` with `scalatest` | `scalacheck`
- `F#` with `xUnit` | `FsCheck`
- `kotlin` with `kotest`
- `C#` with `xUnit` | `LanguageExt` | `FsCheck`

## Resources
- [NIR full specification](https://fr.wikipedia.org/wiki/Num%C3%A9ro_de_s%C3%A9curit%C3%A9_sociale_en_France)
- [Online key calculator](http://nourtier.net/cle_NIR/cle_NIR.htm)

