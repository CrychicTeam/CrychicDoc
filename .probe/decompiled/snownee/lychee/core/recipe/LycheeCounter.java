package snownee.lychee.core.recipe;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public interface LycheeCounter {

    void lychee$setRecipeId(@Nullable ResourceLocation var1);

    @Nullable
    ResourceLocation lychee$getRecipeId();

    void lychee$setCount(int var1);

    int lychee$getCount();

    default void lychee$update(@Nullable ResourceLocation prevRecipeId, Recipe<?> recipe) {
        this.lychee$setRecipeId(recipe.getId());
        if (Objects.equals(prevRecipeId, recipe.getId())) {
            this.lychee$setCount(this.lychee$getCount() + 1);
        } else {
            this.lychee$setCount(0);
        }
    }
}