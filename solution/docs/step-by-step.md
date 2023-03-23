# NIR kata (Solution proposal)
This proposal is documented in `java` but the code is available in other languages (`C#`, `kotlin`, `scala`, `F#`).

## 1) Validate a NIR
Based on the specifications, we already have a test list to start with :

```text
Invalid NIRs
- 2230 // too short
- 323115935012322 // incorrect sex
- 2ab115935012322 // incorrect year
- 223ab5935012322 // incorrect month
- 223145935012322 // incorrect month 2
- 223005935012322 // incorrect month 3
- 22311xx35012322 // incorrect department
- 223119635012322 // incorrect department 2
- 2231159zzz12322 // incorrect city
- 223115935012321 // incorrect control key
- 2231159350123221 // too long

Valid NIRs
- 223115935012322
- 200029923123486
- 254031088723464
- 195017262676215
- 155053933981739
- 106099955391094
```

### Write our first test
:red_circle: Let's start by a failing test (as usual in T.D.D).
`What happens if the client code sends an empty String?`

```java
class ValidateNIR {
	@Test
	void validate_empty_string_should_return_false() {
		assertThat(NIR.validate(""))
				.isFalse();
	}
}
```

Our code is not compiling.
![Compile error is a failing test](img/first-failing-test.png)

:green_circle: Make it `green` as fast as possible by [generating production code from usage](https://xtrem-tdd.netlify.app/Flavours/generate-code-from-usage).

```java
public class NIR {
    public static Boolean validate(String potentialNIR) {
        return false;
    }
}
```

:large_blue_circle: Refactor the code to introduce an empty `String` rule.
```java
public class NIR {
    public static Boolean validate(String potentialNIR) {
        return potentialNIR != "";
    }
}
```

### Too short String
:red_circle: Add a second test case for too short `String`.

```java
@Test
void validate_short_string_should_return_false() {
	assertThat(NIR.validate("2230"))
			.isFalse();
}
```

:green_circle: Add a `length` rule.
```java
public class NIR {
    public static Boolean validate(String potentialNIR) {
		return potentialNIR != "" && potentialNIR.length() == 15;
    }
}
```

:large_blue_circle: Simplify the expression and remove `magic number`.
```java
public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }
}
```

Our test list is now looking like this:
```text
Invalid NIRs
✅empty string
✅ 2230 // too short
- 323115935012322 // incorrect sex
- 2ab115935012322 // incorrect year
- 223ab5935012322 // incorrect month
- 223145935012322 // incorrect month 2
- 223005935012322 // incorrect month 3
- 22311xx35012322 // incorrect department
- 223119635012322 // incorrect department 2
- 2231159zzz12322 // incorrect city
- 223115935012321 // incorrect control key
- 2231159350123221 // too long

Valid NIRs
- 223115935012322
- 200029923123486
- 254031088723464
- 195017262676215
- 155053933981739
- 106099955391094
```

### Incorrect Sex
:red_circle: Let's add a new expectation from our tests.
```java
@Test
void validate_with_invalid_sex_should_return_false() {
	assertThat(NIR.validate("323115935012322"))
			.isFalse();
}
```

:green_circle: Express sex validation.
```java
public static Boolean validate(String potentialNIR) {
	return potentialNIR.length() == VALID_LENGTH
			&& (potentialNIR.charAt(0) == '1' || potentialNIR.charAt(0) == '2');
}
```

:large_blue_circle: Extract named method for better comprehension.
```java
public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR);
    }

    private static boolean validateLength(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }

    private static boolean validateSex(String potentialNIR) {
        return potentialNIR.charAt(0) == '1' || potentialNIR.charAt(0) == '2';
    }
}
```

In refactoring stage, you should always wonder how to improve test code as well.
Here we could use parameterized tests instead of maintaining 1 test method per "test case".



:green_circle:
:red_circle: 
:large_blue_circle: 



Step by step

Faire 2 schémas :
- Primitive Obsession : 
	- montrer les dangers d'avoir 1 String représentant le NIR
	- Validation partout
	- Possibilité d'utiliser 1 String non NIR
- Parse don't validate
	- schéma comme dans Functional Core Imperative Shell