package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GuanoBlock extends FallingBlockWithColor {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public GuanoBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN).strength(0.3F).sound(SoundType.FROGSPAWN), 4204321);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if ((!(entity instanceof LivingEntity) || entity.getFeetBlockState().m_60713_(this)) && isForlornEntity(entity)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.9, 1.0, 0.9));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() != null && (isForlornEntity(entityCollisionContext.getEntity()) || entityCollisionContext.getEntity() instanceof FallingBlockEntity)) {
            return Shapes.block();
        }
        return SHAPE;
    }

    public static boolean isForlornEntity(Entity entity) {
        return entity instanceof Bat;
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
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos blockPos) {
        return 0.2F;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(20) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 1.0).add((double) (randomSource.nextFloat() - 0.5F), (double) (randomSource.nextFloat() * 0.5F + 0.2F), (double) (randomSource.nextFloat() - 0.5F));
            level.addParticle(ACParticleRegistry.FLY.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }
}