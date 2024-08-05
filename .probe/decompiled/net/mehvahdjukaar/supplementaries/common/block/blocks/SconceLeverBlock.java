package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SconceLeverBlock extends SconceWallBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SconceLeverBlock(BlockBehaviour.Properties properties, Supplier<SimpleParticleType> particleData) {
        super(properties, particleData);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWERED, false)).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(LIT, true));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        InteractionResult result = super.m_6227_(state, worldIn, pos, player, handIn, hit);
        if (result.consumesAction()) {
            this.updateNeighbors(state, worldIn, pos);
            return result;
        } else if (worldIn.isClientSide) {
            state.m_61122_(POWERED);
            return InteractionResult.SUCCESS;
        } else {
            BlockState blockstate = this.setPowered(state, worldIn, pos);
            boolean enabled = (Boolean) blockstate.m_61143_(POWERED);
            float f = enabled ? 0.6F : 0.5F;
            worldIn.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            worldIn.m_142346_(player, enabled ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
            return InteractionResult.CONSUME;
        }
    }

    public BlockState setPowered(BlockState state, Level world, BlockPos pos) {
        state = (BlockState) state.m_61122_(POWERED);
        world.setBlock(pos, state, 3);
        this.updateNeighbors(state, world, pos);
        return state;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.m_60713_(newState.m_60734_()) && (Boolean) state.m_61143_(POWERED)) {
            this.updateNeighbors(state, worldIn, pos);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(POWERED) ^ !blockState.m_61143_(LIT) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(POWERED) ^ !blockState.m_61143_(LIT) && getFacing(blockState) == side ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);
        world.updateNeighborsAt(pos.relative(getFacing(state).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    protected static Direction getFacing(BlockState state) {
        return (Direction) state.m_61143_(FACING);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (!(Boolean) stateIn.m_61143_(POWERED)) {
            super.animateTick(stateIn, worldIn, pos, rand);
        } else if ((Boolean) stateIn.m_61143_(LIT)) {
            Direction direction = (Direction) stateIn.m_61143_(FACING);
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_() + 0.65;
            double d2 = (double) pos.m_123343_() + 0.5;
            Direction direction1 = direction.getOpposite();
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.125 * (double) direction1.getStepX(), d1 + 0.15, d2 + 0.125 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 + 0.125 * (double) direction1.getStepX(), d1 + 0.15, d2 + 0.125 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean lightUp(Entity entity, BlockState state, BlockPos pos, LevelAccessor world, ILightable.FireSourceType fireSourceType) {
        boolean ret = super.lightUp(entity, state, pos, world, fireSourceType);
        if (ret && world instanceof ServerLevel level) {
            this.updateNeighbors(state, level, pos);
        }
        return ret;
    }

    @Override
    public boolean extinguish(@Nullable Entity player, BlockState state, BlockPos pos, LevelAccessor world) {
        boolean ret = super.extinguish(player, state, pos, world);
        if (ret && world instanceof ServerLevel level) {
            this.updateNeighbors(state, level, pos);
        }
        return ret;
    }
}