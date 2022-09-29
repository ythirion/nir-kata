## NIR kata
- Write a parser of `NIR` (simplified rules)
- You must respect ["parse don't validate"](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/) principle
- Your parsing function must respect the below property
```scala
  property("isomorphic") = forAll { (nir: NIR) =>
    parseNIR(nir.toString).value == nir
  }
```

- [NIR specification](https://fr.wikipedia.org/wiki/Num%C3%A9ro_de_s%C3%A9curit%C3%A9_sociale_en_France)
  - [Online key calculator](http://nourtier.net/cle_NIR/cle_NIR.htm)
- [Mutation-based Property Driven Development](https://abailly.github.io/posts/mutation-testing.html)

Inspired by [Arnaud Bailly](https://abailly.github.io/about.html)