package io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.configured;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.mods.VillagerTradeMod;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class ConfiguredItemListing implements VillagerTrades.ItemListing {

    final VillagerTrades.ItemListing tradeSource;

    final Supplier<VillagerTradeMod> modSupplier;

    public ConfiguredItemListing(@Nonnull VillagerTrades.ItemListing tradeSource, @Nonnull Supplier<VillagerTradeMod> modSupplier) {
        this.tradeSource = tradeSource;
        this.modSupplier = modSupplier;
    }

    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource random) {
        try {
            int attempts = 0;
            MerchantOffer offer;
            do {
                offer = this.tradeSource.getOffer(trader, random);
            } while (offer == null && attempts++ < 100);
            if (attempts > 1) {
                if (offer == null) {
                    LightmansCurrency.LogError("Original Item Listing Class: " + this.tradeSource.getClass().getName());
                    throw new NullPointerException("The original Item Listing of the converted trade returned a null trade offer " + attempts + " times!");
                }
                LightmansCurrency.LogWarning("Original Item Listing Class: " + this.tradeSource.getClass().getName());
                LightmansCurrency.LogWarning("Converted Trade took " + attempts + " attempts to receive a non-null trade offer from the original Item Listing!");
            }
            assert offer != null;
            VillagerTradeMod mod = (VillagerTradeMod) this.modSupplier.get();
            ItemStack itemA = mod.modifyCost(trader, offer.getBaseCostA());
            ItemStack itemB = mod.modifyCost(trader, offer.getCostB());
            ItemStack itemC = mod.modifyResult(trader, offer.getResult());
            return new MerchantOffer(itemA, itemB, itemC, offer.getUses(), offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier(), offer.getDemand());
        } catch (Throwable var9) {
            LightmansCurrency.LogDebug("Error converting trade:", var9);
            return null;
        }
    }
}