package com.rekindled.embers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;

public class GrandhammerLootModifier extends LootModifier {

    public static final Codec<GrandhammerLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, GrandhammerLootModifier::new));

    public GrandhammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<GrandhammerLootModifier> codec() {
        return CODEC;
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear();
        return generatedLoot;
    }
}