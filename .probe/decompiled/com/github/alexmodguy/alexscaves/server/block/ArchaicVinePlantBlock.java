package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArchaicVinePlantBlock extends GrowingPlantBodyBlock {

    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public ArchaicVinePlantBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).randomTicks().noCollission().instabreak().sound(SoundType.VINE), Direction.DOWN, SHAPE, false);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ACBlockRegistry.ARCHAIC_VINE.get();
    }
}