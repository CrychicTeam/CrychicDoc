package com.simibubi.create.content.kinetics.crusher;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrushingWheelBlock extends RotatedPillarKineticBlock implements IBE<CrushingWheelBlockEntity> {

    public CrushingWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CRUSHING_WHEEL_COLLISION_SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        for (Direction d : Iterate.directions) {
            if (d.getAxis() != state.m_61143_(AXIS) && AllBlocks.CRUSHING_WHEEL_CONTROLLER.has(worldIn.getBlockState(pos.relative(d)))) {
                worldIn.removeBlock(pos.relative(d), isMoving);
            }
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    public void updateControllers(BlockState state, Level world, BlockPos pos, Direction side) {
        if (side.getAxis() != state.m_61143_(AXIS)) {
            if (world != null) {
                BlockPos controllerPos = pos.relative(side);
                BlockPos otherWheelPos = pos.relative(side, 2);
                boolean controllerExists = AllBlocks.CRUSHING_WHEEL_CONTROLLER.has(world.getBlockState(controllerPos));
                boolean controllerIsValid = controllerExists && (Boolean) world.getBlockState(controllerPos).m_61143_(CrushingWheelControllerBlock.VALID);
                Direction controllerOldDirection = controllerExists ? (Direction) world.getBlockState(controllerPos).m_61143_(CrushingWheelControllerBlock.f_52588_) : null;
                boolean controllerShouldExist = false;
                boolean controllerShouldBeValid = false;
                Direction controllerNewDirection = Direction.DOWN;
                BlockState otherState = world.getBlockState(otherWheelPos);
                if (AllBlocks.CRUSHING_WHEEL.has(otherState)) {
                    controllerShouldExist = true;
                    CrushingWheelBlockEntity be = this.getBlockEntity(world, pos);
                    CrushingWheelBlockEntity otherBE = this.getBlockEntity(world, otherWheelPos);
                    if (be != null && otherBE != null && be.getSpeed() > 0.0F != otherBE.getSpeed() > 0.0F && be.getSpeed() != 0.0F) {
                        Direction.Axis wheelAxis = (Direction.Axis) state.m_61143_(AXIS);
                        Direction.Axis sideAxis = side.getAxis();
                        int controllerADO = Math.round(Math.signum(be.getSpeed())) * side.getAxisDirection().getStep();
                        Vec3 controllerDirVec = new Vec3(wheelAxis == Direction.Axis.X ? 1.0 : 0.0, wheelAxis == Direction.Axis.Y ? 1.0 : 0.0, wheelAxis == Direction.Axis.Z ? 1.0 : 0.0).cross(new Vec3(sideAxis == Direction.Axis.X ? 1.0 : 0.0, sideAxis == Direction.Axis.Y ? 1.0 : 0.0, sideAxis == Direction.Axis.Z ? 1.0 : 0.0));
                        controllerNewDirection = Direction.getNearest(controllerDirVec.x * (double) controllerADO, controllerDirVec.y * (double) controllerADO, controllerDirVec.z * (double) controllerADO);
                        controllerShouldBeValid = true;
                    }
                    if (otherState.m_61143_(AXIS) != state.m_61143_(AXIS)) {
                        controllerShouldExist = false;
                    }
                }
                if (!controllerShouldExist) {
                    if (controllerExists) {
                        world.setBlockAndUpdate(controllerPos, Blocks.AIR.defaultBlockState());
                    }
                } else {
                    if (!controllerExists) {
                        if (!world.getBlockState(controllerPos).m_247087_()) {
                            return;
                        }
                        world.setBlockAndUpdate(controllerPos, (BlockState) ((BlockState) AllBlocks.CRUSHING_WHEEL_CONTROLLER.getDefaultState().m_61124_(CrushingWheelControllerBlock.VALID, controllerShouldBeValid)).m_61124_(CrushingWheelControllerBlock.f_52588_, controllerNewDirection));
                    } else if (controllerIsValid != controllerShouldBeValid || controllerOldDirection != controllerNewDirection) {
                        world.setBlockAndUpdate(controllerPos, (BlockState) ((BlockState) world.getBlockState(controllerPos).m_61124_(CrushingWheelControllerBlock.VALID, controllerShouldBeValid)).m_61124_(CrushingWheelControllerBlock.f_52588_, controllerNewDirection));
                    }
                    ((CrushingWheelControllerBlock) AllBlocks.CRUSHING_WHEEL_CONTROLLER.get()).updateSpeed(world.getBlockState(controllerPos), world, controllerPos);
                }
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn.getY() < (double) ((float) pos.m_123342_() + 1.25F)) && entityIn.onGround()) {
            float speed = (Float) this.getBlockEntityOptional(worldIn, pos).map(KineticBlockEntity::getSpeed).orElse(0.0F);
            double x = 0.0;
            double z = 0.0;
            if (state.m_61143_(AXIS) == Direction.Axis.X) {
                z = (double) (speed / 20.0F);
                x += ((double) ((float) pos.m_123341_() + 0.5F) - entityIn.getX()) * 0.1F;
            }
            if (state.m_61143_(AXIS) == Direction.Axis.Z) {
                x = (double) (speed / -20.0F);
                z += ((double) ((float) pos.m_123343_() + 0.5F) - entityIn.getZ()) * 0.1F;
            }
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(x, 0.0, z));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        for (Direction direction : Iterate.directions) {
            BlockPos neighbourPos = pos.relative(direction);
            BlockState neighbourState = worldIn.m_8055_(neighbourPos);
            Direction.Axis stateAxis = (Direction.Axis) state.m_61143_(AXIS);
            if (AllBlocks.CRUSHING_WHEEL_CONTROLLER.has(neighbourState) && direction.getAxis() != stateAxis) {
                return false;
            }
            if (AllBlocks.CRUSHING_WHEEL.has(neighbourState) && (neighbourState.m_61143_(AXIS) != stateAxis || stateAxis != direction.getAxis())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(AXIS);
    }

    @Override
    public float getParticleTargetRadius() {
        return 1.125F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 1.0F;
    }

    @Override
    public Class<CrushingWheelBlockEntity> getBlockEntityClass() {
        return CrushingWheelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CrushingWheelBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CrushingWheelBlockEntity>) AllBlockEntityTypes.CRUSHING_WHEEL.get();
    }
}