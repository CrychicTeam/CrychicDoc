package net.minecraftforge.event.enchanting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

public class EnchantmentLevelSetEvent extends Event {

    private final Level level;

    private final BlockPos pos;

    private final int enchantRow;

    private final int power;

    @NotNull
    private final ItemStack itemStack;

    private final int originalLevel;

    private int enchantLevel;

    public EnchantmentLevelSetEvent(Level level, BlockPos pos, int enchantRow, int power, @NotNull ItemStack itemStack, int enchantLevel) {
        this.level = level;
        this.pos = pos;
        this.enchantRow = enchantRow;
        this.power = power;
        this.itemStack = itemStack;
        this.originalLevel = enchantLevel;
        this.enchantLevel = enchantLevel;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getEnchantRow() {
        return this.enchantRow;
    }

    public int getPower() {
        return this.power;
    }

    @NotNull
    public ItemStack getItem() {
        return this.itemStack;
    }

    public int getOriginalLevel() {
        return this.originalLevel;
    }

    public int getEnchantLevel() {
        return this.enchantLevel;
    }

    public void setEnchantLevel(int level) {
        this.enchantLevel = level;
    }
}