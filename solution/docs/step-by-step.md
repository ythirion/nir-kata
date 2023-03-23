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
✅ empty string
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
We have already some duplication in our tests. We could use parameterized tests instead of maintaining 1 test method per "test case".

With `junit`, we can create `Parameterized Tests` by using `junit-jupiter-params`.

```java
class ValidateNIR {
    public static Stream<Arguments> invalidNIRs() {
        return Stream.of(
                Arguments.of("", "empty string"),
                Arguments.of("2230", "too short"),
                Arguments.of("323115935012322", "incorrect sex")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNIRs")
    void should_return_false(String input, String reason) {
        assertThat(NIR.validate(input))
                .isFalse()
                .as(reason);
    }
}
```

Test output is now looking like this:
![Parameterized test output](img/test-output.png)

### Incorrect Year
:red_circle: Continue to add feature from our test list.

```java
public static Stream<Arguments> invalidNIRs() {
	return Stream.of(
		...
		Arguments.of("2ab115935012322", "incorrect year")
	);
}
```

> Be careful when using `Parameterized tests` to move 1 test case at a time.

:green_circle: Make it pass by hardcoding year validation. 

```java
public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR)
                && validateYear(potentialNIR);
    }

    private static boolean validateLength(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }

    private static boolean validateSex(String potentialNIR) {
        return potentialNIR.charAt(0) == '1' || potentialNIR.charAt(0) == '2';
    }

    private static boolean validateYear(String potentialNIR) {
        return false;
    }
}
```

:large_blue_circle: Implement year validation as expressed in specification.
I choose to use Regex to check whether `Year is a valid number or not`.
```java
private static boolean validateYear(String potentialNIR) {
	return potentialNIR
			.substring(1, 3)
			.matches("[0-9.]+");
}
```

The whole class can still be improved -> pass only the needed characters for inner validation.
```java
public class NIR {
	private static final int VALID_LENGTH = 15;
	private static final char MALE = '1', FEMALE = '2';

	public static Boolean validate(String potentialNIR) {
		return validateLength(potentialNIR)
				&& validateSex(potentialNIR.charAt(0))
				&& validateYear(potentialNIR.substring(1, 3));
	}

	private static boolean validateLength(String potentialNIR) {
		return potentialNIR.length() == VALID_LENGTH;
	}

	private static boolean validateSex(char sex) {
		return sex == MALE || sex == FEMALE;
	}

	private static boolean validateYear(String year) {
		return isANumber(year);
	}

	private static boolean isANumber(String potentialNumber) {
		return potentialNumber.matches("[0-9.]+");
	}
}
```

```text
Invalid NIRs
✅ empty string
✅ 2230 // too short
✅ 323115935012322 // incorrect sex
✅ 2ab115935012322 // incorrect year
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

### Fast Forward invalid NIRs
```text
Invalid NIRs
✅ empty string
✅ 2230 // too short
✅ 323115935012322 // incorrect sex
✅ 2ab115935012322 // incorrect year
✅ 223ab5935012322 // incorrect month
✅ 223145935012322 // incorrect month 2
✅ 223005935012322 // incorrect month 3
✅ 22311xx35012322 // incorrect department
✅ 223119635012322 // incorrect department 2
✅ 2231159zzz12322 // incorrect city
✅ 223115935012321 // incorrect control key
✅ 2231159350123221 // too long

Valid NIRs
- 223115935012322
- 200029923123486
- 254031088723464
- 195017262676215
- 155053933981739
- 106099955391094
```

Here are some interesting stuff made during the implementation.
- Use `lombok` to define `Extension methods` on `String`
- Use `vavr` to use a more functional and less imperative way of coding

```java
@UtilityClass
public class StringExtensions {
    // Extension methods are static methods with at least 1 parameter
	// The first parameter type is the one we extend
    public static Option<Integer> toInt(String potentialNumber) {
        return isANumber(potentialNumber) // Use Option<Integer> -> equivalent to Optional since java 8
                ? some(Integer.parseInt(potentialNumber))
                : none();
    }

    private static boolean isANumber(String str) {
        return str != null && str.matches("[0-9.]+");
    }
}
```

- Use `String` extension methods from our production code

```java
@UtilityClass
// Reference extension classes
@ExtensionMethod(StringExtensions.class)
public class NIR {
	...

    private static boolean validateMonth(String month) {
        return validateNumber(month, x -> x > 0 && x <= 12);
    }

    private static boolean validateDepartment(String department) {
        return validateNumber(department, x -> x > 0 && (x <= 95 || x == 99));
    }

    // A generic method that parses a String then apply a validation function on it to check whether the value ensures it
	// Here is its signature: String -> (int -> bool) -> bool  
    private static boolean validateNumber(String potentialNumber, Function<Integer, Boolean> isValid) {
        return potentialNumber
                .toInt() // return an Option<Integer> (Some if something or None)
                .map(isValid) // called only if Some
                .getOrElse(false); // if none returns false
    }

    private static boolean isANumber(String potentialNumber) {
        return potentialNumber.matches("[0-9.]+");
    }
}
```

Faire 2 schémas :
- Primitive Obsession : 
	- montrer les dangers d'avoir 1 String représentant le NIR
	- Validation partout
	- Possibilité d'utiliser 1 String non NIR
- Parse don't validate
	- schéma comme dans Functional Core Imperative Shell