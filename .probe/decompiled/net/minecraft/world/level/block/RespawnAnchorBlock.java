package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RespawnAnchorBlock extends Block {

    public static final int MIN_CHARGES = 0;

    public static final int MAX_CHARGES = 4;

    public static final IntegerProperty CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;

    private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));

    private static final ImmutableList<Vec3i> RESPAWN_OFFSETS = new Builder().addAll(RESPAWN_HORIZONTAL_OFFSETS).addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::m_7495_).iterator()).addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::m_7494_).iterator()).add(new Vec3i(0, 1, 0)).build();

    public RespawnAnchorBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CHARGE, 0));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        if (interactionHand4 == InteractionHand.MAIN_HAND && !isRespawnFuel($$6) && isRespawnFuel(player3.m_21120_(InteractionHand.OFF_HAND))) {
            return InteractionResult.PASS;
        } else if (isRespawnFuel($$6) && canBeCharged(blockState0)) {
            charge(player3, level1, blockPos2, blockState0);
            if (!player3.getAbilities().instabuild) {
                $$6.shrink(1);
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else if ((Integer) blockState0.m_61143_(CHARGE) == 0) {
            return InteractionResult.PASS;
        } else if (!canSetSpawn(level1)) {
            if (!level1.isClientSide) {
                this.explode(blockState0, level1, blockPos2);
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            if (!level1.isClientSide) {
                ServerPlayer $$7 = (ServerPlayer) player3;
                if ($$7.getRespawnDimension() != level1.dimension() || !blockPos2.equals($$7.getRespawnPosition())) {
                    $$7.setRespawnPosition(level1.dimension(), blockPos2, 0.0F, false, true);
                    level1.playSound(null, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }
    }

    private static boolean isRespawnFuel(ItemStack itemStack0) {
        return itemStack0.is(Items.GLOWSTONE);
    }

    private static boolean canBeCharged(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(CHARGE) < 4;
    }

    private static boolean isWaterThatWouldFlow(BlockPos blockPos0, Level level1) {
        FluidState $$2 = level1.getFluidState(blockPos0);
        if (!$$2.is(FluidTags.WATER)) {
            return false;
        } else if ($$2.isSource()) {
            return true;
        } else {
            float $$3 = (float) $$2.getAmount();
            if ($$3 < 2.0F) {
                return false;
            } else {
                FluidState $$4 = level1.getFluidState(blockPos0.below());
                return !$$4.is(FluidTags.WATER);
            }
        }
    }

    private void explode(BlockState blockState0, Level level1, final BlockPos blockPos2) {
        level1.removeBlock(blockPos2, false);
        boolean $$3 = Direction.Plane.HORIZONTAL.stream().map(blockPos2::m_121945_).anyMatch(p_55854_ -> isWaterThatWouldFlow(p_55854_, level1));
        final boolean $$4 = $$3 || level1.getFluidState(blockPos2.above()).is(FluidTags.WATER);
        ExplosionDamageCalculator $$5 = new ExplosionDamageCalculator() {

            @Override
            public Optional<Float> getBlockExplosionResistance(Explosion p_55904_, BlockGetter p_55905_, BlockPos p_55906_, BlockState p_55907_, FluidState p_55908_) {
                return p_55906_.equals(blockPos2) && $$4 ? Optional.of(Blocks.WATER.getExplosionResistance()) : super.getBlockExplosionResistance(p_55904_, p_55905_, p_55906_, p_55907_, p_55908_);
            }
        };
        Vec3 $$6 = blockPos2.getCenter();
        level1.explode(null, level1.damageSources().badRespawnPointExplosion($$6), $$5, $$6, 5.0F, true, Level.ExplosionInteraction.BLOCK);
    }

    public static boolean canSetSpawn(Level level0) {
        return level0.dimensionType().respawnAnchorWorks();
    }

    public static void charge(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3) {
        BlockState $$4 = (BlockState) blockState3.m_61124_(CHARGE, (Integer) blockState3.m_61143_(CHARGE) + 1);
        level1.setBlock(blockPos2, $$4, 3);
        level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of(entity0, $$4));
        level1.playSound(null, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(CHARGE) != 0) {
            if (randomSource3.nextInt(100) == 0) {
                level1.playSound(null, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            double $$4 = (double) blockPos2.m_123341_() + 0.5 + (0.5 - randomSource3.nextDouble());
            double $$5 = (double) blockPos2.m_123342_() + 1.0;
            double $$6 = (double) blockPos2.m_123343_() + 0.5 + (0.5 - randomSource3.nextDouble());
            double $$7 = (double) randomSource3.nextFloat() * 0.04;
            level1.addParticle(ParticleTypes.REVERSE_PORTAL, $$4, $$5, $$6, 0.0, $$7, 0.0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(CHARGE);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    public static int getScaledChargeLevel(BlockState blockState0, int int1) {
        return Mth.floor((float) ((Integer) blockState0.m_61143_(CHARGE) - 0) / 4.0F * (float) int1);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return getScaledChargeLevel(blockState0, 15);
    }

    public static Optional<Vec3> findStandUpPosition(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2) {
        Optional<Vec3> $$3 = findStandUpPosition(entityType0, collisionGetter1, blockPos2, true);
        return $$3.isPresent() ? $$3 : findStandUpPosition(entityType0, collisionGetter1, blockPos2, false);
    }

    private static Optional<Vec3> findStandUpPosition(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2, boolean boolean3) {
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        UnmodifiableIterator var5 = RESPAWN_OFFSETS.iterator();
        while (var5.hasNext()) {
            Vec3i $$5 = (Vec3i) var5.next();
            $$4.set(blockPos2).move($$5);
            Vec3 $$6 = DismountHelper.findSafeDismountLocation(entityType0, collisionGetter1, $$4, boolean3);
            if ($$6 != null) {
                return Optional.of($$6);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}