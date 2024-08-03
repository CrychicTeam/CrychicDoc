package mezz.jei.library.recipes.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.UnmodifiableView;

public class IngredientToRecipesMap<R> {

    private final Map<String, List<R>> uidToRecipes = new HashMap();

    public void add(R recipe, List<String> ingredientUids) {
        for (String uid : ingredientUids) {
            List<R> recipes = (List<R>) this.uidToRecipes.computeIfAbsent(uid, k -> new ArrayList());
            recipes.add(recipe);
        }
    }

    @UnmodifiableView
    public List<R> get(String ingredientUid) {
        List<R> recipes = (List<R>) this.uidToRecipes.get(ingredientUid);
        return recipes == null ? Collections.emptyList() : Collections.unmodifiableList(recipes);
    }
}