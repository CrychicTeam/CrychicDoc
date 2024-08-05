package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.OfferModification;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public abstract class TransformableTrade<T extends VillagerTrades.ItemListing> implements VillagerTrades.ItemListing {

    protected final TradeItem firstInput;

    protected final TradeItem secondInput;

    protected int maxUses = 16;

    protected int villagerExperience = 2;

    protected float priceMultiplier = 0.05F;

    @Nullable
    private TransformableTrade.Transformer transformer;

    public TransformableTrade(TradeItem[] inputs) {
        Preconditions.checkArgument(1 <= inputs.length && inputs.length <= 2, "Inputs must be 1 or 2 items");
        this.firstInput = inputs[0];
        this.secondInput = inputs.length == 2 ? inputs[1] : TradeItem.EMPTY;
    }

    @Nullable
    @Override
    public final MerchantOffer getOffer(Entity entity, RandomSource random) {
        MerchantOffer offer = this.createOffer(entity, random);
        if (offer == null) {
            return null;
        } else {
            if (this.transformer != null) {
                this.transformer.accept(new OfferModification(offer), entity, random);
            }
            return offer;
        }
    }

    @Nullable
    public abstract MerchantOffer createOffer(Entity var1, RandomSource var2);

    public T transform(TransformableTrade.Transformer offerModification) {
        this.transformer = offerModification;
        return this.getSelf();
    }

    public T maxUses(int maxUses) {
        this.maxUses = maxUses;
        return this.getSelf();
    }

    public T villagerExperience(int villagerExperience) {
        this.villagerExperience = villagerExperience;
        return this.getSelf();
    }

    public T priceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
        return this.getSelf();
    }

    protected T getSelf() {
        return (T) this;
    }

    protected MerchantOffer createOffer(ItemStack output, RandomSource random) {
        ItemStack fi = this.firstInput.createItemStack(random);
        ItemStack si = this.secondInput.createItemStack(random);
        return new MerchantOffer(fi, si, output, this.maxUses, this.villagerExperience, this.priceMultiplier);
    }

    public interface Transformer {

        void accept(OfferModification var1, Entity var2, RandomSource var3);
    }
}