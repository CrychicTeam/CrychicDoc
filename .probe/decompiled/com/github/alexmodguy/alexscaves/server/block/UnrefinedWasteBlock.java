package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class UnrefinedWasteBlock extends FallingBlockWithColor {

    protected static final VoxelShape SHAPE = Block.box(0.1, 0.1, 0.1, 15.9, 14.0, 15.9);

    public UnrefinedWasteBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME).strength(0.5F).sound(ACSoundTypes.UNREFINED_WASTE).lightLevel(state -> 3).emissiveRendering((state, level, pos) -> true), 60928);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof LivingEntity living && !entity.getType().is(ACTagRegistry.RESISTS_RADIATION)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.9, 1.0, 0.9));
            living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 4000));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(2) == 0) {
            Direction direction = Direction.getRandom(randomSource);
            BlockPos blockpos = blockPos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            if (!blockState.m_60815_() || !blockstate.m_60783_(level, blockpos, direction.getOpposite())) {
                double d0 = direction.getStepX() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepX() * 0.6;
                double d1 = direction.getStepY() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepY() * 0.6;
                double d2 = direction.getStepZ() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.6;
                level.addParticle(randomSource.nextBoolean() ? ACParticleRegistry.GAMMAROACH.get() : ACParticleRegistry.HAZMAT_BREATHE.get(), (double) blockPos.m_123341_() + d0, (double) blockPos.m_123342_() + d1, (double) blockPos.m_123343_() + d2, 0.0, 0.1 + (double) (level.random.nextFloat() * 0.1F), 0.0);
            }
        }
    }
}