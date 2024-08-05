package org.violetmoon.quark.mixin.mixins;

import com.google.common.collect.ImmutableMap.Builder;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.tweaks.module.BeachVillagersModule;

@Mixin({ VillagerTrades.class })
public class VillagerTradesMixin {

    @ModifyExpressionValue(method = { "lambda$static$0" }, require = 1, at = { @At(value = "INVOKE", ordinal = 0, target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;") })
    private static Builder<VillagerType, Item> addBeachType(Builder<VillagerType, Item> original) {
        original.put(BeachVillagersModule.beach, Items.JUNGLE_BOAT);
        return original;
    }
}