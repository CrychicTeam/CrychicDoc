package net.minecraft.world.level.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TargetBlock extends Block {

    private static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;

    private static final int ACTIVATION_TICKS_ARROWS = 20;

    private static final int ACTIVATION_TICKS_OTHER = 8;

    public TargetBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(OUTPUT_POWER, 0));
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        int $$4 = updateRedstoneOutput(level0, blockState1, blockHitResult2, projectile3);
        if (projectile3.getOwner() instanceof ServerPlayer $$6) {
            $$6.m_36220_(Stats.TARGET_HIT);
            CriteriaTriggers.TARGET_BLOCK_HIT.trigger($$6, projectile3, blockHitResult2.m_82450_(), $$4);
        }
    }

    private static int updateRedstoneOutput(LevelAccessor levelAccessor0, BlockState blockState1, BlockHitResult blockHitResult2, Entity entity3) {
        int $$4 = getRedstoneStrength(blockHitResult2, blockHitResult2.m_82450_());
        int $$5 = entity3 instanceof AbstractArrow ? 20 : 8;
        if (!levelAccessor0.getBlockTicks().m_183582_(blockHitResult2.getBlockPos(), blockState1.m_60734_())) {
            setOutputPower(levelAccessor0, blockState1, $$4, blockHitResult2.getBlockPos(), $$5);
        }
        return $$4;
    }

    private static int getRedstoneStrength(BlockHitResult blockHitResult0, Vec3 vec1) {
        Direction $$2 = blockHitResult0.getDirection();
        double $$3 = Math.abs(Mth.frac(vec1.x) - 0.5);
        double $$4 = Math.abs(Mth.frac(vec1.y) - 0.5);
        double $$5 = Math.abs(Mth.frac(vec1.z) - 0.5);
        Direction.Axis $$6 = $$2.getAxis();
        double $$7;
        if ($$6 == Direction.Axis.Y) {
            $$7 = Math.max($$3, $$5);
        } else if ($$6 == Direction.Axis.Z) {
            $$7 = Math.max($$3, $$4);
        } else {
            $$7 = Math.max($$4, $$5);
        }
        return Math.max(1, Mth.ceil(15.0 * Mth.clamp((0.5 - $$7) / 0.5, 0.0, 1.0)));
    }

    private static void setOutputPower(LevelAccessor levelAccessor0, BlockState blockState1, int int2, BlockPos blockPos3, int int4) {
        levelAccessor0.m_7731_(blockPos3, (BlockState) blockState1.m_61124_(OUTPUT_POWER, int2), 3);
        levelAccessor0.scheduleTick(blockPos3, blockState1.m_60734_(), int4);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(OUTPUT_POWER) != 0) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(OUTPUT_POWER, 0), 3);
        }
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return (Integer) blockState0.m_61143_(OUTPUT_POWER);
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(OUTPUT_POWER);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!level1.isClientSide() && !blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Integer) blockState0.m_61143_(OUTPUT_POWER) > 0 && !level1.m_183326_().m_183582_(blockPos2, this)) {
                level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(OUTPUT_POWER, 0), 18);
            }
        }
    }
}