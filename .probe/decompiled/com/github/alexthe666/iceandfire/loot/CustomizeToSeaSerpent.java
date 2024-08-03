package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class CustomizeToSeaSerpent extends LootItemConditionalFunction {

    public CustomizeToSeaSerpent(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    public ItemStack run(ItemStack stack, @NotNull LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntitySeaSerpent seaSerpent) {
            int ancientModifier = seaSerpent.isAncient() ? 2 : 1;
            if (stack.getItem() instanceof ItemSeaSerpentScales) {
                stack.setCount(1 + seaSerpent.m_217043_().nextInt(1 + (int) Math.ceil((double) (seaSerpent.getSeaSerpentScale() * 3.0F * (float) ancientModifier))));
                return new ItemStack(seaSerpent.getEnum().scale.get(), stack.getCount());
            }
            if (stack.getItem() == IafItemRegistry.SERPENT_FANG.get()) {
                stack.setCount(1 + seaSerpent.m_217043_().nextInt(1 + (int) Math.ceil((double) (seaSerpent.getSeaSerpentScale() * 2.0F * (float) ancientModifier))));
                return stack;
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_SERPENT;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToSeaSerpent> {

        public void serialize(@NotNull JsonObject object, @NotNull CustomizeToSeaSerpent functionClazz, @NotNull JsonSerializationContext serializationContext) {
        }

        @NotNull
        public CustomizeToSeaSerpent deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, @NotNull LootItemCondition[] conditionsIn) {
            return new CustomizeToSeaSerpent(conditionsIn);
        }
    }
}