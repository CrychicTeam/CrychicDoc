package com.almostreliable.morejs.features.villager;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class OfferModification {

    private final MerchantOffer offer;

    private final OfferExtension offerAsAccessor;

    public OfferModification(MerchantOffer offer) {
        this.offer = offer;
        this.offerAsAccessor = (OfferExtension) offer;
    }

    public ItemStack getFirstInput() {
        return this.offerAsAccessor.morejs$getFirstInput();
    }

    public void setFirstInput(ItemStack itemStack) {
        this.offerAsAccessor.morejs$setFirstInput(itemStack);
    }

    public ItemStack getSecondInput() {
        return this.offerAsAccessor.morejs$getSecondInput();
    }

    public void setSecondInput(ItemStack itemStack) {
        this.offerAsAccessor.morejs$setSecondInput(itemStack);
    }

    public ItemStack getOutput() {
        return this.offerAsAccessor.morejs$getOutput();
    }

    public void setOutput(ItemStack itemStack) {
        this.offerAsAccessor.morejs$setOutput(itemStack);
    }

    public int getMaxUses() {
        return this.offer.getMaxUses();
    }

    public void setMaxUses(int maxUses) {
        this.offerAsAccessor.morejs$setMaxUses(maxUses);
    }

    public int getDemand() {
        return this.offer.getDemand();
    }

    public void setDemand(int demand) {
        this.offerAsAccessor.morejs$setDemand(demand);
    }

    public int getVillagerExperience() {
        return this.offer.getXp();
    }

    public void setVillagerExperience(int villagerExperience) {
        this.offerAsAccessor.morejs$setVillagerExperience(villagerExperience);
    }

    public float getPriceMultiplier() {
        return this.offer.getPriceMultiplier();
    }

    public void setPriceMultiplier(float priceMultiplier) {
        this.offerAsAccessor.morejs$setPriceMultiplier(priceMultiplier);
    }

    public void setRewardExp(boolean rewardExp) {
        this.offerAsAccessor.morejs$setRewardExp(rewardExp);
    }

    public boolean isRewardingExp() {
        return this.offerAsAccessor.morejs$isRewardingExp();
    }

    public MerchantOffer getMerchantOffer() {
        return this.offer;
    }
}