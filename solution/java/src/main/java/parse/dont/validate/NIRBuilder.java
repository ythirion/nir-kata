package parse.dont.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

@With
@Getter
@AllArgsConstructor
public class NIRBuilder {
    private final Sex sex;
    private Year year;
    private Month month;

    public NIRBuilder(Sex sex) {
        this.sex = sex;
    }
}
