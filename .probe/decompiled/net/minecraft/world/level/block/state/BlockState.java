package net.minecraft.world.level.block.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockState extends BlockBehaviour.BlockStateBase {

    public static final Codec<BlockState> CODEC = m_61127_(BuiltInRegistries.BLOCK.m_194605_(), Block::m_49966_).stable();

    public BlockState(Block block0, ImmutableMap<Property<?>, Comparable<?>> immutableMapPropertyComparable1, MapCodec<BlockState> mapCodecBlockState2) {
        super(block0, immutableMapPropertyComparable1, mapCodecBlockState2);
    }

    @Override
    protected BlockState asState() {
        return this;
    }
}