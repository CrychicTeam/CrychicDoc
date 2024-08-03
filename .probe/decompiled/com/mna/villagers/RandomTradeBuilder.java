package com.mna.villagers;

import java.util.Random;
import java.util.function.Function;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class RandomTradeBuilder {

    protected Function<Random, ItemStack> price = null;

    protected Function<Random, ItemStack> price2 = random -> ItemStack.EMPTY;

    protected Function<Random, ItemStack> forSale = null;

    protected final int maxTrades;

    protected final int xp;

    protected final float priceMult;

    public RandomTradeBuilder(int maxTrades, int xp, float priceMult) {
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMult = priceMult;
    }

    public RandomTradeBuilder setPrice(Function<Random, ItemStack> price) {
        this.price = price;
        return this;
    }

    public RandomTradeBuilder setPrice(Item item, int min, int max) {
        return this.setPrice(createFunction(item, min, max));
    }

    public RandomTradeBuilder setPrice2(Function<Random, ItemStack> price2) {
        this.price2 = price2;
        return this;
    }

    public RandomTradeBuilder setPrice2(Item item, int min, int max) {
        return this.setPrice2(createFunction(item, min, max));
    }

    public RandomTradeBuilder setForSale(Function<Random, ItemStack> forSale) {
        this.forSale = forSale;
        return this;
    }

    public RandomTradeBuilder setForSale(Item item, int min, int max) {
        return this.setForSale(createFunction(item, min, max));
    }

    public RandomTradeBuilder setEmeraldPrice(int emeralds) {
        return this.setPrice(random -> new ItemStack(Items.EMERALD, emeralds));
    }

    public RandomTradeBuilder setEmeraldPriceFor(int emeralds, Item item, int amt) {
        this.setEmeraldPrice(emeralds);
        return this.setForSale(random -> new ItemStack(item, amt));
    }

    public RandomTradeBuilder setEmeraldPriceFor(int emeralds, Item item) {
        return this.setEmeraldPriceFor(emeralds, item, 1);
    }

    public RandomTradeBuilder setEmeraldPrice(int min, int max) {
        return this.setPrice(Items.EMERALD, min, max);
    }

    public RandomTradeBuilder setEmeraldPriceFor(int min, int max, Item item, int amt) {
        this.setEmeraldPrice(min, max);
        return this.setForSale(random -> new ItemStack(item, amt));
    }

    public RandomTradeBuilder setEmeraldPriceFor(int min, int max, Item item) {
        return this.setEmeraldPriceFor(min, max, item, 1);
    }

    public boolean canBuild() {
        return this.price != null && this.forSale != null;
    }

    public VillagerTrades.ItemListing build() {
        Random rnd = new Random();
        return (entity, random) -> !this.canBuild() ? null : new MerchantOffer((ItemStack) this.price.apply(rnd), (ItemStack) this.price2.apply(rnd), (ItemStack) this.forSale.apply(rnd), this.maxTrades, this.xp, this.priceMult);
    }

    public static Function<Random, ItemStack> createFunction(Item item, int min, int max) {
        return random -> new ItemStack(item, random.nextInt(max) + min);
    }
}