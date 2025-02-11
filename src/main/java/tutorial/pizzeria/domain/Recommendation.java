package tutorial.pizzeria.domain;

public enum Recommendation {

    YES("Yes"),
    NO("No");

    private final String displayName;

    Recommendation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
