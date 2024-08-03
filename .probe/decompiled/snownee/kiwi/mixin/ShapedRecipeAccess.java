package snownee.kiwi.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ ShapedRecipe.class })
public interface ShapedRecipeAccess {

    @Invoker
    static Map<String, Ingredient> callKeyFromJson(JsonObject jsonObject0) {
        throw new IllegalStateException();
    }

    @Invoker
    static String[] callPatternFromJson(JsonArray jsonArray0) {
        throw new IllegalStateException();
    }

    @Invoker
    static String[] callShrink(String... string0) {
        throw new IllegalStateException();
    }

    @Invoker
    static NonNullList<Ingredient> callDissolvePattern(String[] string0, Map<String, Ingredient> mapStringIngredient1, int int2, int int3) {
        throw new IllegalStateException();
    }
}