package net.minecraft.world.level.material;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmptyFluid extends Fluid {

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2, Fluid fluid3, Direction direction4) {
        return true;
    }

    @Override
    public Vec3 getFlow(BlockGetter blockGetter0, BlockPos blockPos1, FluidState fluidState2) {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(LevelReader levelReader0) {
        return 0;
    }

    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 0.0F;
    }

    @Override
    public float getHeight(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return 0.0F;
    }

    @Override
    public float getOwnHeight(FluidState fluidState0) {
        return 0.0F;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState0) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(FluidState fluidState0) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState0) {
        return 0;
    }

    @Override
    public VoxelShape getShape(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.empty();
    }
}