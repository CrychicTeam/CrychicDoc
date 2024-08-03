package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MuckBlock extends FallingBlockWithColor {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);

    public MuckBlock(BlockBehaviour.Properties props) {
        super(props, 6052974);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getFeetBlockState().m_60713_(this)) {
            if (this.isOceanEntity(entity)) {
                entity.setPos(entity.getX(), (double) ((float) blockPos.m_123342_() + 1.0F), entity.getZ());
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.1, 0.0));
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.85, 1.0, 0.85));
            }
        }
    }

    private boolean isOceanEntity(Entity entity) {
        if (entity.getType().is(ACTagRegistry.SEAFLOOR_DENIZENS)) {
            return true;
        } else {
            if (entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.FEET).is(ACItemRegistry.DIVING_BOOTS.get())) {
                return true;
            }
            return false;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() != null && (this.isOceanEntity(entityCollisionContext.getEntity()) || entityCollisionContext.getEntity() instanceof FallingBlockEntity)) {
            return Shapes.block();
        }
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
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos blockPos) {
        return 0.2F;
    }
}