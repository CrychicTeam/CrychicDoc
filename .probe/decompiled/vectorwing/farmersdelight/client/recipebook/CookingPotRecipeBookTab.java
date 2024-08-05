package vectorwing.farmersdelight.client.recipebook;

public enum CookingPotRecipeBookTab {

    MEALS("meals"), DRINKS("drinks"), MISC("misc");

    public final String name;

    private CookingPotRecipeBookTab(String name) {
        this.name = name;
    }

    public static CookingPotRecipeBookTab findByName(String name) {
        for (CookingPotRecipeBookTab value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    public String toString() {
        return this.name;
    }
}