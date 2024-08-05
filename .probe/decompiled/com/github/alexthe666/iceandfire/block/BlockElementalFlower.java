package com.github.alexthe666.iceandfire.block;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockElementalFlower extends BushBlock {

    public Item itemBlock;

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public BlockElementalFlower() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noOcclusion().noCollission().dynamicShape().randomTicks().sound(SoundType.GRASS));
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        Block block = state.m_60734_();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || state.m_204336_(BlockTags.SAND);
    }

    public boolean canStay(Level worldIn, BlockPos pos) {
        BlockState soil = worldIn.getBlockState(pos.below());
        if (this == IafBlockRegistry.FIRE_LILY.get()) {
            return soil.m_204336_(BlockTags.SAND) || soil.m_60713_(Blocks.NETHERRACK);
        } else {
            return this == IafBlockRegistry.LIGHTNING_LILY.get() ? soil.m_204336_(BlockTags.DIRT) || soil.m_60713_(Blocks.GRASS) : soil.m_204336_(BlockTags.ICE) || soil.m_204336_(BlockTags.SNOW) || soil.m_204336_(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON);
        }
    }

    public void updateTick(Level worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(Level worldIn, BlockPos pos) {
        if (!this.canStay(worldIn, pos)) {
            worldIn.m_46961_(pos, true);
            return false;
        } else {
            return true;
        }
    }

    protected boolean canSustainBush(BlockState state) {
        return true;
    }
}