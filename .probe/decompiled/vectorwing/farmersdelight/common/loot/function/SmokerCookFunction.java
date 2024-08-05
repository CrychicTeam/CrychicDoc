package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction {

    public static final ResourceLocation ID = new ResourceLocation("farmersdelight", "smoker_cook");

    protected SmokerCookFunction(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (stack.isEmpty()) {
            return stack;
        } else {
            Optional<SmokingRecipe> recipe = context.getLevel().getRecipeManager().<Container, SmokingRecipe>getAllRecipesFor(RecipeType.SMOKING).stream().filter(r -> ((Ingredient) r.m_7527_().get(0)).test(stack)).findFirst();
            if (recipe.isPresent()) {
                ItemStack result = ((SmokingRecipe) recipe.get()).m_8043_(context.getLevel().m_9598_()).copy();
                result.setCount(result.getCount() * stack.getCount());
                return result;
            } else {
                return stack;
            }
        }
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctions.SMOKER_COOK.get();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SmokerCookFunction> {

        public SmokerCookFunction deserialize(JsonObject _object, JsonDeserializationContext _context, LootItemCondition[] conditionsIn) {
            return new SmokerCookFunction(conditionsIn);
        }
    }
}