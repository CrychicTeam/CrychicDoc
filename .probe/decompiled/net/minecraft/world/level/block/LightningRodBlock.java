package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class LightningRodBlock extends RodBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final int ACTIVATION_TICKS = 8;

    public static final int RANGE = 128;

    private static final int SPARK_CYCLE = 200;

    public LightningRodBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.UP)).m_61124_(WATERLOGGED, false)).m_61124_(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        boolean $$2 = $$1.getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, blockPlaceContext0.m_43719_())).m_61124_(WATERLOGGED, $$2);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) && blockState0.m_61143_(f_52588_) == direction3 ? 15 : 0;
    }

    public void onLightningStrike(BlockState blockState0, Level level1, BlockPos blockPos2) {
        level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, true), 3);
        this.updateNeighbours(blockState0, level1, blockPos2);
        level1.m_186460_(blockPos2, this, 8);
        level1.m_46796_(3002, blockPos2, ((Direction) blockState0.m_61143_(f_52588_)).getAxis().ordinal());
    }

    private void updateNeighbours(BlockState blockState0, Level level1, BlockPos blockPos2) {
        level1.updateNeighborsAt(blockPos2.relative(((Direction) blockState0.m_61143_(f_52588_)).getOpposite()), this);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(POWERED, false), 3);
        this.updateNeighbours(blockState0, serverLevel1, blockPos2);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (level1.isThundering() && (long) level1.random.nextInt(200) <= level1.getGameTime() % 200L && blockPos2.m_123342_() == level1.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos2.m_123341_(), blockPos2.m_123343_()) - 1) {
            ParticleUtils.spawnParticlesAlongAxis(((Direction) blockState0.m_61143_(f_52588_)).getAxis(), level1, blockPos2, 0.125, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Boolean) blockState0.m_61143_(POWERED)) {
                this.updateNeighbours(blockState0, level1, blockPos2);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Boolean) blockState0.m_61143_(POWERED) && !level1.m_183326_().m_183582_(blockPos2, this)) {
                level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, false), 18);
            }
        }
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        if (level0.isThundering() && projectile3 instanceof ThrownTrident && ((ThrownTrident) projectile3).isChanneling()) {
            BlockPos $$4 = blockHitResult2.getBlockPos();
            if (level0.m_45527_($$4)) {
                LightningBolt $$5 = EntityType.LIGHTNING_BOLT.create(level0);
                if ($$5 != null) {
                    $$5.m_20219_(Vec3.atBottomCenterOf($$4.above()));
                    Entity $$6 = projectile3.getOwner();
                    $$5.setCause($$6 instanceof ServerPlayer ? (ServerPlayer) $$6 : null);
                    level0.m_7967_($$5);
                }
                level0.playSound(null, $$4, SoundEvents.TRIDENT_THUNDER, SoundSource.WEATHER, 5.0F, 1.0F);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52588_, POWERED, WATERLOGGED);
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }
}