package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FallingGuanoEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GuanoLayerBlock extends SnowLayerBlock implements Fallable {

    public GuanoLayerBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN).strength(0.3F).sound(SoundType.FROGSPAWN).forceSolidOff().randomTicks());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos1) {
        levelAccessor.scheduleTick(pos, this, this.getDelayAfterPlace());
        return super.updateShape(blockState, direction, blockState1, levelAccessor, pos, pos1);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.m_43722_().isEmpty() && context.m_43722_().is(this.m_5456_()) ? state.m_60734_() instanceof SnowLayerBlock && (Integer) state.m_61143_(f_56581_) < 8 : false;
    }

    @Override
    public void tick(BlockState state, ServerLevel blockState, BlockPos blockPos, RandomSource randomSource) {
        if (isFree(blockState.m_8055_(blockPos.below())) && blockPos.m_123342_() >= blockState.m_141937_()) {
            FallingGuanoEntity.fall(blockState, blockPos, state);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean b) {
        level.m_186460_(pos, this, this.getDelayAfterPlace());
    }

    public static boolean isFree(BlockState belowState) {
        return belowState.m_60734_() instanceof SnowLayerBlock && belowState.m_61143_(f_56581_) < 8 ? true : FallingBlock.isFree(belowState);
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos fallenOn, FallingBlockEntity fallingBlockEntity) {
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if ((!(entity instanceof LivingEntity) || entity.getFeetBlockState().m_60713_(this)) && GuanoBlock.isForlornEntity(entity)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.9, 1.0, 0.9));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60713_(this)) {
            int i = (Integer) blockstate.m_61143_(f_56581_);
            return (BlockState) blockstate.m_61124_(f_56581_, Math.min(8, i + 1));
        } else {
            return this.m_49966_();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() != null && (GuanoBlock.isForlornEntity(entityCollisionContext.getEntity()) || entityCollisionContext.getEntity() instanceof FallingBlockEntity)) {
            return super.getShape(state, level, blockPos, context);
        }
        return super.getCollisionShape(state, level, blockPos, context);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(40) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 1.0).add((double) (randomSource.nextFloat() - 0.5F), (double) (randomSource.nextFloat() * 0.5F + 0.2F), (double) (randomSource.nextFloat() - 0.5F));
            level.addParticle(ACParticleRegistry.FLY.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }
}