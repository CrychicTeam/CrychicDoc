package com.simibubi.create.content.logistics.funnel;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;

public abstract class AbstractFunnelBlock extends Block implements IBE<FunnelBlockEntity>, IWrenchable, ProperWaterloggedBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected AbstractFunnelBlock(BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(WATERLOGGED, false));
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new ReducedDestroyEffects());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.withWater((BlockState) this.m_49966_().m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos())), context);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED, WATERLOGGED));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            InvManipulationBehaviour behaviour = BlockEntityBehaviour.get(worldIn, pos, InvManipulationBehaviour.TYPE);
            if (behaviour != null) {
                behaviour.onNeighborChanged(fromPos);
            }
            if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
                worldIn.m_186460_(pos, this, 0);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
        if (previouslyPowered != worldIn.m_276867_(pos)) {
            worldIn.m_7731_(pos, (BlockState) state.m_61122_(POWERED), 2);
        }
    }

    public static ItemStack tryInsert(Level worldIn, BlockPos pos, ItemStack toInsert, boolean simulate) {
        FilteringBehaviour filter = BlockEntityBehaviour.get(worldIn, pos, FilteringBehaviour.TYPE);
        InvManipulationBehaviour inserter = BlockEntityBehaviour.get(worldIn, pos, InvManipulationBehaviour.TYPE);
        if (inserter == null) {
            return toInsert;
        } else if (filter != null && !filter.test(toInsert)) {
            return toInsert;
        } else {
            if (simulate) {
                inserter.simulate();
            }
            ItemStack insert = inserter.insert(toInsert);
            if (!simulate && insert.getCount() != toInsert.getCount() && worldIn.getBlockEntity(pos) instanceof FunnelBlockEntity funnelBlockEntity) {
                funnelBlockEntity.onTransfer(toInsert);
                if (funnelBlockEntity.hasFlap()) {
                    funnelBlockEntity.flap(true);
                }
            }
            return insert;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Block block = world.m_8055_(pos.relative(getFunnelFacing(state).getOpposite())).m_60734_();
        return !(block instanceof AbstractFunnelBlock);
    }

    @Nullable
    public static boolean isFunnel(BlockState state) {
        return state.m_60734_() instanceof AbstractFunnelBlock;
    }

    @Nullable
    public static Direction getFunnelFacing(BlockState state) {
        return !(state.m_60734_() instanceof AbstractFunnelBlock) ? null : ((AbstractFunnelBlock) state.m_60734_()).getFacing(state);
    }

    protected abstract Direction getFacing(BlockState var1);

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_() && !isFunnel(newState) || !newState.m_155947_()) {
            IBE.onRemove(state, world, pos, newState);
        }
    }

    @Override
    public Class<FunnelBlockEntity> getBlockEntityClass() {
        return FunnelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FunnelBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends FunnelBlockEntity>) AllBlockEntityTypes.FUNNEL.get();
    }
}