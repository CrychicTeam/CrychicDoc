package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

public class EnchantedItemTrade extends TransformableTrade<EnchantedItemTrade> {

    private final Item itemToEnchant;

    List<Enchantment> enchantments;

    private int minEnchantmentAmount = 1;

    private int maxEnchantmentAmount = 1;

    public EnchantedItemTrade(TradeItem[] inputs, Item itemToEnchant) {
        super(inputs);
        this.itemToEnchant = itemToEnchant;
        this.enchantments = BuiltInRegistries.ENCHANTMENT.stream().toList();
    }

    public EnchantedItemTrade enchantments(Enchantment... enchantments) {
        this.enchantments = Arrays.stream(enchantments).peek(e -> {
            if (e == null) {
                ConsoleJS.SERVER.error("Null enchantment in array: " + Arrays.toString(enchantments));
            }
        }).filter(Objects::nonNull).toList();
        return this;
    }

    public EnchantedItemTrade amount(int min, int max) {
        this.minEnchantmentAmount = min;
        this.maxEnchantmentAmount = max;
        return this;
    }

    public EnchantedItemTrade amount(int amount) {
        return this.amount(amount, amount);
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        ItemStack result = this.itemToEnchant.equals(Items.BOOK) ? new ItemStack(Items.ENCHANTED_BOOK) : new ItemStack(this.itemToEnchant);
        int amount = Mth.nextInt(random, this.minEnchantmentAmount, this.maxEnchantmentAmount);
        for (int i = 0; i < amount; i++) {
            Enchantment enchantment = (Enchantment) this.enchantments.get(random.nextInt(this.enchantments.size()));
            int level = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            if (result.is(Items.ENCHANTED_BOOK)) {
                EnchantmentInstance enchantmentInstance = new EnchantmentInstance(enchantment, level);
                EnchantedBookItem.addEnchantment(result, enchantmentInstance);
            } else {
                result.enchant(enchantment, level);
            }
        }
        return this.createOffer(result, random);
    }
}