package com.simibubi.create.foundation.utility;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ColorHandlers {

    public static BlockColor getGrassyBlock() {
        return (state, world, pos, layer) -> pos != null && world != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.get(0.5, 1.0);
    }

    public static ItemColor getGrassyItem() {
        return (stack, layer) -> GrassColor.get(0.5, 1.0);
    }

    public static BlockColor getRedstonePower() {
        return (state, world, pos, layer) -> RedStoneWireBlock.getColorForPower(pos != null && world != null ? (Integer) state.m_61143_(BlockStateProperties.POWER) : 0);
    }
}