package mezz.jei.api.ingredients.subtypes;

@FunctionalInterface
public interface IIngredientSubtypeInterpreter<T> {

    String NONE = "";

    String apply(T var1, UidContext var2);
}