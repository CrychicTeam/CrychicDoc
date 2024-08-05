package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class SmeltItemFunction extends LootItemConditionalFunction {

    private static final Logger LOGGER = LogUtils.getLogger();

    SmeltItemFunction(LootItemCondition[] lootItemCondition0) {
        super(lootItemCondition0);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.FURNACE_SMELT;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.isEmpty()) {
            return itemStack0;
        } else {
            Optional<SmeltingRecipe> $$2 = lootContext1.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack0), lootContext1.getLevel());
            if ($$2.isPresent()) {
                ItemStack $$3 = ((SmeltingRecipe) $$2.get()).m_8043_(lootContext1.getLevel().m_9598_());
                if (!$$3.isEmpty()) {
                    return $$3.copyWithCount(itemStack0.getCount());
                }
            }
            LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", itemStack0);
            return itemStack0;
        }
    }

    public static LootItemConditionalFunction.Builder<?> smelted() {
        return m_80683_(SmeltItemFunction::new);
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SmeltItemFunction> {

        public SmeltItemFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            return new SmeltItemFunction(lootItemCondition2);
        }
    }
}