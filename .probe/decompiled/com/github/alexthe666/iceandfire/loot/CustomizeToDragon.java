package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonFlesh;
import com.github.alexthe666.iceandfire.item.ItemDragonScales;
import com.github.alexthe666.iceandfire.item.ItemDragonSkull;
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

public class CustomizeToDragon extends LootItemConditionalFunction {

    public CustomizeToDragon(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected ItemStack run(ItemStack stack, @NotNull LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityDragonBase dragon) {
            if (stack.getItem() == IafItemRegistry.DRAGON_BONE.get()) {
                stack.setCount(1 + dragon.m_217043_().nextInt(1 + dragon.getAgeInDays() / 25));
                return stack;
            }
            if (stack.getItem() instanceof ItemDragonScales) {
                stack.setCount(dragon.getAgeInDays() / 25 + dragon.m_217043_().nextInt(1 + dragon.getAgeInDays() / 5));
                return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
            }
            if (stack.getItem() instanceof ItemDragonEgg) {
                if (dragon.shouldDropLoot()) {
                    return new ItemStack(dragon.getVariantEgg(dragon.getVariant()), stack.getCount());
                }
                stack.setCount(1 + dragon.m_217043_().nextInt(1 + dragon.getAgeInDays() / 5));
                return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
            }
            if (stack.getItem() instanceof ItemDragonFlesh) {
                return new ItemStack(dragon.getFleshItem(), 1 + dragon.m_217043_().nextInt(1 + dragon.getAgeInDays() / 25));
            }
            if (stack.getItem() instanceof ItemDragonSkull) {
                ItemStack skull = dragon.getSkull();
                skull.setCount(stack.getCount());
                skull.setTag(stack.getTag());
                return skull;
            }
            if (stack.is(IafItemTags.DRAGON_BLOODS)) {
                return new ItemStack(dragon.getBloodItem(), stack.getCount());
            }
            if (stack.is(IafItemTags.DRAGON_HEARTS)) {
                return new ItemStack(dragon.getHeartItem(), stack.getCount());
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_DRAGON;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToDragon> {

        public void serialize(@NotNull JsonObject object, @NotNull CustomizeToDragon functionClazz, @NotNull JsonSerializationContext serializationContext) {
        }

        @NotNull
        public CustomizeToDragon deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, @NotNull LootItemCondition[] conditionsIn) {
            return new CustomizeToDragon(conditionsIn);
        }
    }
}