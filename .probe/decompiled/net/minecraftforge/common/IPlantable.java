package net.minecraftforge.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlantable {

    default PlantType getPlantType(BlockGetter level, BlockPos pos) {
        if (this instanceof CropBlock || this == Blocks.PITCHER_CROP) {
            return PlantType.CROP;
        } else if (this instanceof SaplingBlock) {
            return PlantType.PLAINS;
        } else if (this instanceof FlowerBlock) {
            return PlantType.PLAINS;
        } else if (this == Blocks.DEAD_BUSH) {
            return PlantType.DESERT;
        } else if (this == Blocks.LILY_PAD) {
            return PlantType.WATER;
        } else if (this == Blocks.RED_MUSHROOM) {
            return PlantType.CAVE;
        } else if (this == Blocks.BROWN_MUSHROOM) {
            return PlantType.CAVE;
        } else if (this == Blocks.NETHER_WART) {
            return PlantType.NETHER;
        } else {
            return this == Blocks.TALL_GRASS ? PlantType.PLAINS : PlantType.PLAINS;
        }
    }

    BlockState getPlant(BlockGetter var1, BlockPos var2);
}