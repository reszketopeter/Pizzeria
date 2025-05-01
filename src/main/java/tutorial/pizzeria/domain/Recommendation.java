package tutorial.pizzeria.domain;

import lombok.Getter;

@Getter
public enum Recommendation {

    YES("Yes"),
    NO("No");

    private final String displayName;

    Recommendation(String displayName) {
        this.displayName = displayName;
    }

}
