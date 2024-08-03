package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.trading.MerchantOffer;

public class PotionTrade extends TransformableTrade<PotionTrade> {

    List<Potion> potions;

    private Item itemForPotion = Items.POTION;

    private boolean onlyBrewablePotion;

    private boolean noBrewablePotion;

    public PotionTrade(TradeItem[] inputs) {
        super(inputs);
        this.potions = BuiltInRegistries.POTION.m_123024_().toList();
    }

    public PotionTrade item(Item item) {
        this.itemForPotion = item;
        return this;
    }

    public PotionTrade potions(Potion... potions) {
        this.potions = Arrays.stream(potions).peek(e -> {
            if (e == null) {
                ConsoleJS.SERVER.error("Null potion in array: " + Arrays.toString(potions));
            }
        }).filter(Objects::nonNull).toList();
        return this;
    }

    public PotionTrade onlyBrewablePotion() {
        this.onlyBrewablePotion = true;
        return this;
    }

    public PotionTrade noBrewablePotion() {
        this.noBrewablePotion = false;
        return this;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        List<Potion> allowedPotions = this.potions.stream().filter(p -> {
            if (p.getEffects().isEmpty()) {
                return false;
            } else if (this.onlyBrewablePotion) {
                return PotionBrewing.isBrewablePotion(p);
            } else {
                return this.noBrewablePotion ? !PotionBrewing.isBrewablePotion(p) : true;
            }
        }).toList();
        if (allowedPotions.isEmpty()) {
            return null;
        } else {
            Potion potion = (Potion) allowedPotions.get(random.nextInt(this.potions.size()));
            ItemStack potionStack = PotionUtils.setPotion(new ItemStack(this.itemForPotion), potion);
            return this.createOffer(potionStack, random);
        }
    }
}