package dev.architectury.registry.level.entity.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public record SimpleTrade(ItemStack primaryPrice, ItemStack secondaryPrice, ItemStack sale, int maxTrades, int experiencePoints, float priceMultiplier) implements VillagerTrades.ItemListing {

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource random) {
        return new MerchantOffer(this.primaryPrice, this.secondaryPrice, this.sale, this.maxTrades, this.experiencePoints, this.priceMultiplier);
    }
}