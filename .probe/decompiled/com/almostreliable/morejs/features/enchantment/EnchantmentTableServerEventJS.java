package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.features.villager.IntRange;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

public class EnchantmentTableServerEventJS extends EnchantmentTableEventJS {

    protected final EnchantmentMenuProcess state;

    private final BlockPos pos;

    private boolean itemChanged;

    public EnchantmentTableServerEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state) {
        super(item, secondItem, level, player, state.getMenu());
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public EnchantmentTableServerEventJS.Data get(int index) {
        Preconditions.checkElementIndex(index, this.getSize());
        return new EnchantmentTableServerEventJS.Data(index);
    }

    public int getSize() {
        return this.state.getMenu().costs.length;
    }

    public void setItem(ItemStack item) {
        this.itemChanged = true;
        this.item = item;
    }

    public boolean itemWasChanged() {
        return this.itemChanged;
    }

    public class Data {

        protected final int index;

        protected Data(int index) {
            this.index = index;
        }

        public int getRequiredLevel() {
            return EnchantmentTableServerEventJS.this.menu.costs[this.index];
        }

        public int getEnchantmentCount() {
            return this.getEnchantments().size();
        }

        public void forEachEnchantments(BiConsumer<Enchantment, Integer> consumer) {
            this.getEnchantments().forEach(i -> consumer.accept(i.enchantment, i.level));
        }

        public void clearEnchantments() {
            this.getEnchantments().clear();
        }

        public List<ResourceLocation> getEnchantmentIds() {
            return this.getEnchantments().stream().map(e -> BuiltInRegistries.ENCHANTMENT.getKey(e.enchantment)).filter(Objects::nonNull).toList();
        }

        public boolean hasEnchantment(ResourceLocation id) {
            return this.hasEnchantment(id, IntRange.all());
        }

        public boolean hasEnchantment(ResourceLocation id, IntRange range) {
            Enchantment enchantment = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional(id).orElse(null);
            if (enchantment == null) {
                return false;
            } else {
                for (EnchantmentInstance enchantmentInstance : this.getEnchantments()) {
                    if (enchantmentInstance.enchantment == enchantment && range.test(enchantmentInstance.level)) {
                        return true;
                    }
                }
                return false;
            }
        }

        protected List<EnchantmentInstance> getEnchantments() {
            return EnchantmentTableServerEventJS.this.state.getEnchantments(this.index);
        }
    }
}