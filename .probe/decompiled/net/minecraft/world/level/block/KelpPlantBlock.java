package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;

public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {

    protected KelpPlantBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, Direction.UP, Shapes.block(), true);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) Blocks.KELP;
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    protected boolean canAttachTo(BlockState blockState0) {
        return this.getHeadBlock().m_142209_(blockState0);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        return false;
    }
}