package com.simibubi.create.content.kinetics.turntable;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TurntableBlock extends KineticBlock implements IBE<TurntableBlockEntity> {

    public TurntableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.TURNTABLE_SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity e) {
        if (e.onGround()) {
            if (!(e.getDeltaMovement().y > 0.0)) {
                if (!(e.getY() < (double) ((float) pos.m_123342_() + 0.5F))) {
                    this.withBlockEntityDo(worldIn, pos, be -> {
                        float speed = be.getSpeed() * 3.0F / 10.0F;
                        if (speed != 0.0F) {
                            Level world = e.getCommandSenderWorld();
                            if (world.isClientSide && e instanceof Player && worldIn.getBlockState(e.blockPosition()) != state) {
                                Vec3 origin = VecHelper.getCenterOf(pos);
                                Vec3 offset = e.position().subtract(origin);
                                offset = VecHelper.rotate(offset, (double) (Mth.clamp(speed, -16.0F, 16.0F) / 1.0F), Direction.Axis.Y);
                                Vec3 movement = origin.add(offset).subtract(e.position());
                                e.setDeltaMovement(e.getDeltaMovement().add(movement));
                                e.hurtMarked = true;
                            }
                            if (!(e instanceof Player)) {
                                if (!world.isClientSide) {
                                    if (e instanceof LivingEntity) {
                                        float diff = e.getYHeadRot() - speed;
                                        ((LivingEntity) e).setNoActionTime(20);
                                        e.setYBodyRot(diff);
                                        e.setYHeadRot(diff);
                                        e.setOnGround(false);
                                        e.hurtMarked = true;
                                    }
                                    e.setYRot(e.getYRot() - speed);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<TurntableBlockEntity> getBlockEntityClass() {
        return TurntableBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TurntableBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends TurntableBlockEntity>) AllBlockEntityTypes.TURNTABLE.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}