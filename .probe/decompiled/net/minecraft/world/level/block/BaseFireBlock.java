package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BaseFireBlock extends Block {

    private static final int SECONDS_ON_FIRE = 8;

    private final float fireDamage;

    protected static final float AABB_OFFSET = 1.0F;

    protected static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public BaseFireBlock(BlockBehaviour.Properties blockBehaviourProperties0, float float1) {
        super(blockBehaviourProperties0);
        this.fireDamage = float1;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return getState(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos());
    }

    public static BlockState getState(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockPos $$2 = blockPos1.below();
        BlockState $$3 = blockGetter0.getBlockState($$2);
        return SoulFireBlock.canSurviveOnBlock($$3) ? Blocks.SOUL_FIRE.defaultBlockState() : ((FireBlock) Blocks.FIRE).getStateForPlacement(blockGetter0, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return DOWN_AABB;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(24) == 0) {
            level1.playLocalSound((double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + randomSource3.nextFloat(), randomSource3.nextFloat() * 0.7F + 0.3F, false);
        }
        BlockPos $$4 = blockPos2.below();
        BlockState $$5 = level1.getBlockState($$4);
        if (!this.canBurn($$5) && !$$5.m_60783_(level1, $$4, Direction.UP)) {
            if (this.canBurn(level1.getBlockState(blockPos2.west()))) {
                for (int $$10 = 0; $$10 < 2; $$10++) {
                    double $$11 = (double) blockPos2.m_123341_() + randomSource3.nextDouble() * 0.1F;
                    double $$12 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
                    double $$13 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, $$11, $$12, $$13, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(level1.getBlockState(blockPos2.east()))) {
                for (int $$14 = 0; $$14 < 2; $$14++) {
                    double $$15 = (double) (blockPos2.m_123341_() + 1) - randomSource3.nextDouble() * 0.1F;
                    double $$16 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
                    double $$17 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, $$15, $$16, $$17, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(level1.getBlockState(blockPos2.north()))) {
                for (int $$18 = 0; $$18 < 2; $$18++) {
                    double $$19 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                    double $$20 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
                    double $$21 = (double) blockPos2.m_123343_() + randomSource3.nextDouble() * 0.1F;
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, $$19, $$20, $$21, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(level1.getBlockState(blockPos2.south()))) {
                for (int $$22 = 0; $$22 < 2; $$22++) {
                    double $$23 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                    double $$24 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
                    double $$25 = (double) (blockPos2.m_123343_() + 1) - randomSource3.nextDouble() * 0.1F;
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, $$23, $$24, $$25, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(level1.getBlockState(blockPos2.above()))) {
                for (int $$26 = 0; $$26 < 2; $$26++) {
                    double $$27 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                    double $$28 = (double) (blockPos2.m_123342_() + 1) - randomSource3.nextDouble() * 0.1F;
                    double $$29 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, $$27, $$28, $$29, 0.0, 0.0, 0.0);
                }
            }
        } else {
            for (int $$6 = 0; $$6 < 3; $$6++) {
                double $$7 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                double $$8 = (double) blockPos2.m_123342_() + randomSource3.nextDouble() * 0.5 + 0.5;
                double $$9 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                level1.addParticle(ParticleTypes.LARGE_SMOKE, $$7, $$8, $$9, 0.0, 0.0, 0.0);
            }
        }
    }

    protected abstract boolean canBurn(BlockState var1);

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!entity3.fireImmune()) {
            entity3.setRemainingFireTicks(entity3.getRemainingFireTicks() + 1);
            if (entity3.getRemainingFireTicks() == 0) {
                entity3.setSecondsOnFire(8);
            }
        }
        entity3.hurt(level1.damageSources().inFire(), this.fireDamage);
        super.m_7892_(blockState0, level1, blockPos2, entity3);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            if (inPortalDimension(level1)) {
                Optional<PortalShape> $$5 = PortalShape.findEmptyPortalShape(level1, blockPos2, Direction.Axis.X);
                if ($$5.isPresent()) {
                    ((PortalShape) $$5.get()).createPortalBlocks();
                    return;
                }
            }
            if (!blockState0.m_60710_(level1, blockPos2)) {
                level1.removeBlock(blockPos2, false);
            }
        }
    }

    private static boolean inPortalDimension(Level level0) {
        return level0.dimension() == Level.OVERWORLD || level0.dimension() == Level.NETHER;
    }

    @Override
    protected void spawnDestroyParticles(Level level0, Player player1, BlockPos blockPos2, BlockState blockState3) {
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide()) {
            level0.m_5898_(null, 1009, blockPos1, 0);
        }
        super.playerWillDestroy(level0, blockPos1, blockState2, player3);
    }

    public static boolean canBePlacedAt(Level level0, BlockPos blockPos1, Direction direction2) {
        BlockState $$3 = level0.getBlockState(blockPos1);
        return !$$3.m_60795_() ? false : getState(level0, blockPos1).m_60710_(level0, blockPos1) || isPortal(level0, blockPos1, direction2);
    }

    private static boolean isPortal(Level level0, BlockPos blockPos1, Direction direction2) {
        if (!inPortalDimension(level0)) {
            return false;
        } else {
            BlockPos.MutableBlockPos $$3 = blockPos1.mutable();
            boolean $$4 = false;
            for (Direction $$5 : Direction.values()) {
                if (level0.getBlockState($$3.set(blockPos1).move($$5)).m_60713_(Blocks.OBSIDIAN)) {
                    $$4 = true;
                    break;
                }
            }
            if (!$$4) {
                return false;
            } else {
                Direction.Axis $$6 = direction2.getAxis().isHorizontal() ? direction2.getCounterClockWise().getAxis() : Direction.Plane.HORIZONTAL.getRandomAxis(level0.random);
                return PortalShape.findEmptyPortalShape(level0, blockPos1, $$6).isPresent();
            }
        }
    }
}