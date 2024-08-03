package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NozzleBlock extends WrenchableDirectionalBlock implements IBE<NozzleBlockEntity> {

    public NozzleBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(f_52588_, context.m_43719_());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.NOZZLE.get((Direction) state.m_61143_(f_52588_));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (fromPos.equals(pos.relative(((Direction) state.m_61143_(f_52588_)).getOpposite())) && !this.canSurvive(state, worldIn, pos)) {
                worldIn.m_46961_(pos, true);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction towardsFan = ((Direction) state.m_61143_(f_52588_)).getOpposite();
        BlockEntity be = worldIn.m_7702_(pos.relative(towardsFan));
        return be instanceof IAirCurrentSource && ((IAirCurrentSource) be).getAirflowOriginSide() == towardsFan.getOpposite();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<NozzleBlockEntity> getBlockEntityClass() {
        return NozzleBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NozzleBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends NozzleBlockEntity>) AllBlockEntityTypes.NOZZLE.get();
    }
}