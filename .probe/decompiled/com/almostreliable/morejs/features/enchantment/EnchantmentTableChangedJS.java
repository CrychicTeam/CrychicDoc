package com.almostreliable.morejs.features.enchantment;

import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventResult;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

public class EnchantmentTableChangedJS extends EnchantmentTableServerEventJS {

    private final RandomSource random;

    public EnchantmentTableChangedJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, EnchantmentMenuProcess state, RandomSource random) {
        super(item, secondItem, level, pos, state.getPlayer(), state);
        this.random = random;
    }

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);
        for (int i = 0; i < this.getSize(); i++) {
            EnchantmentTableChangedJS.MutableData data = this.get(i);
            if (data.getEnchantments().isEmpty()) {
                data.setRequiredLevel(0);
            }
        }
    }

    public EnchantmentTableChangedJS.MutableData get(int index) {
        Preconditions.checkElementIndex(index, this.getSize());
        return new EnchantmentTableChangedJS.MutableData(index);
    }

    @Override
    public int getSize() {
        return this.state.getMenu().costs.length;
    }

    public class MutableData extends EnchantmentTableServerEventJS.Data {

        private MutableData(int index) {
            super(index);
        }

        public void setRequiredLevel(int level) {
            EnchantmentTableChangedJS.this.state.getMenu().costs[this.index] = level;
        }

        public void updateClue() {
            List<EnchantmentInstance> enchantments = this.getEnchantments();
            EnchantmentInstance instance = (EnchantmentInstance) enchantments.get(EnchantmentTableChangedJS.this.random.nextInt(enchantments.size()));
            EnchantmentTableChangedJS.this.menu.enchantClue[this.index] = BuiltInRegistries.ENCHANTMENT.getId(instance.enchantment);
            EnchantmentTableChangedJS.this.menu.levelClue[this.index] = instance.level;
        }

        public void removeEnchantments(BiPredicate<Enchantment, Integer> consumer) {
            this.getEnchantments().removeIf(i -> consumer.test(i.enchantment, i.level));
        }

        public void addEnchantment(Enchantment enchantment, int level) {
            Objects.requireNonNull(enchantment, "Enchantment does not exist");
            this.getEnchantments().add(new EnchantmentInstance(enchantment, level));
        }
    }
}