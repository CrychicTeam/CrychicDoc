package org.violetmoon.zeta.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlockUtils {

    public static boolean isWoodBased(BlockState state) {
        NoteBlockInstrument noteBlockInstrument = state.m_280603_();
        SoundType soundType = state.m_60827_();
        return noteBlockInstrument == NoteBlockInstrument.BASS || soundType == SoundType.BAMBOO_WOOD || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.WOOD;
    }

    public static boolean isGlassBased(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.m_60734_().propagatesSkylightDown(blockState, blockGetter, blockPos) || blockState.m_60827_() == SoundType.GLASS;
    }

    public static boolean isStoneBased(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.m_280603_() == NoteBlockInstrument.BASEDRUM || blockState.m_284242_(blockGetter, blockPos) == MapColor.STONE;
    }

    public static boolean canFallThrough(BlockState state) {
        Block block = state.m_60734_();
        return state.m_60795_() || block == Blocks.FIRE || state.m_278721_() || state.m_247087_();
    }
}