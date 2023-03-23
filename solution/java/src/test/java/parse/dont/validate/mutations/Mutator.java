package parse.dont.validate.mutations;

import io.vavr.Function1;
import io.vavr.test.Gen;
import parse.dont.validate.NIR;

import java.util.Random;

public record Mutator(String name, Function1<NIR, Gen<String>> func) {
    private static final Random random = new Random();

    public String mutate(NIR nir) {
        return func.apply(nir).apply(random);
    }
}