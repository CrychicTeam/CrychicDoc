package com.simibubi.create.content.kinetics.simpleRelays;

import com.simibubi.create.content.kinetics.base.IRotate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ICogWheel extends IRotate {

    static boolean isSmallCog(BlockState state) {
        return isSmallCog(state.m_60734_());
    }

    static boolean isLargeCog(BlockState state) {
        return isLargeCog(state.m_60734_());
    }

    static boolean isSmallCog(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isSmallCog();
    }

    static boolean isLargeCog(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isLargeCog();
    }

    static boolean isDedicatedCogWheel(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isDedicatedCogWheel();
    }

    static boolean isDedicatedCogItem(ItemStack test) {
        Item item = test.getItem();
        return !(item instanceof BlockItem) ? false : isDedicatedCogWheel(((BlockItem) item).getBlock());
    }

    static boolean isSmallCogItem(ItemStack test) {
        Item item = test.getItem();
        return !(item instanceof BlockItem) ? false : isSmallCog(((BlockItem) item).getBlock());
    }

    static boolean isLargeCogItem(ItemStack test) {
        Item item = test.getItem();
        return !(item instanceof BlockItem) ? false : isLargeCog(((BlockItem) item).getBlock());
    }

    default boolean isLargeCog() {
        return false;
    }

    default boolean isSmallCog() {
        return !this.isLargeCog();
    }

    default boolean isDedicatedCogWheel() {
        return false;
    }
}