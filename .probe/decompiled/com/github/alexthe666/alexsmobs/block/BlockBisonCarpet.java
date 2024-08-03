package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBisonCarpet extends CarpetBlock {

    protected static final VoxelShape SELECTION_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public BlockBisonCarpet() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.6F, 1.0F).sound(SoundType.WOOL));
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SELECTION_SHAPE;
    }
}