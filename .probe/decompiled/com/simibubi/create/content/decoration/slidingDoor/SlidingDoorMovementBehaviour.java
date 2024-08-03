package com.simibubi.create.content.decoration.slidingDoor;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.elevator.ElevatorColumn;
import com.simibubi.create.content.contraptions.elevator.ElevatorContraption;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

public class SlidingDoorMovementBehaviour implements MovementBehaviour {

    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }

    @Override
    public boolean mustTickWhileDisabled() {
        return true;
    }

    @Override
    public void tick(MovementContext context) {
        StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo) context.contraption.getBlocks().get(context.localPos);
        if (structureBlockInfo != null) {
            boolean open = SlidingDoorBlockEntity.isOpen(structureBlockInfo.state());
            if (!context.world.isClientSide()) {
                this.tickOpen(context, open);
            }
            Map<BlockPos, BlockEntity> tes = context.contraption.presentBlockEntities;
            if (tes.get(context.localPos) instanceof SlidingDoorBlockEntity sdbe) {
                boolean var7 = sdbe.animation.settled();
                sdbe.animation.chase(open ? 1.0 : 0.0, 0.15F, LerpedFloat.Chaser.LINEAR);
                sdbe.animation.tickChaser();
                if (!var7 && sdbe.animation.settled() && !open) {
                    context.world.playLocalSound(context.position.x, context.position.y, context.position.z, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 0.125F, 1.0F, false);
                }
            }
        }
    }

    protected void tickOpen(MovementContext context, boolean currentlyOpen) {
        boolean shouldOpen = this.shouldOpen(context);
        if (this.shouldUpdate(context, shouldOpen)) {
            if (currentlyOpen != shouldOpen) {
                BlockPos pos = context.localPos;
                Contraption contraption = context.contraption;
                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos);
                if (info != null && info.state().m_61138_(DoorBlock.OPEN)) {
                    this.toggleDoor(pos, contraption, info);
                    if (shouldOpen) {
                        context.world.playSound(null, BlockPos.containing(context.position), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 0.125F, 1.0F);
                    }
                }
            }
        }
    }

    private void toggleDoor(BlockPos pos, Contraption contraption, StructureTemplate.StructureBlockInfo info) {
        BlockState newState = (BlockState) info.state().m_61122_(DoorBlock.OPEN);
        contraption.entity.setBlock(pos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
        BlockPos otherPos = newState.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(otherPos);
        if (info != null && info.state().m_61138_(DoorBlock.OPEN)) {
            newState = (BlockState) info.state().m_61122_(DoorBlock.OPEN);
            contraption.entity.setBlock(otherPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
            contraption.invalidateColliders();
        }
    }

    protected boolean shouldUpdate(MovementContext context, boolean shouldOpen) {
        if (context.firstMovement && shouldOpen) {
            return false;
        } else if (!context.data.contains("Open")) {
            context.data.putBoolean("Open", shouldOpen);
            return true;
        } else {
            boolean wasOpen = context.data.getBoolean("Open");
            context.data.putBoolean("Open", shouldOpen);
            return wasOpen != shouldOpen;
        }
    }

    protected boolean shouldOpen(MovementContext context) {
        if (context.disabled) {
            return false;
        } else {
            Contraption contraption;
            boolean var10000;
            label56: {
                contraption = context.contraption;
                label46: if (!(context.motion.length() < 0.0078125) || contraption.entity.isStalled()) {
                    if (contraption instanceof ElevatorContraption ec && ec.arrived) {
                        break label46;
                    }
                    var10000 = false;
                    break label56;
                }
                var10000 = true;
            }
            boolean canOpen = var10000;
            if (!canOpen) {
                context.temporaryData = null;
                return false;
            } else {
                if (context.temporaryData instanceof WeakReference<?> wr && wr.get() instanceof DoorControlBehaviour dcb && dcb.blockEntity != null && !dcb.blockEntity.m_58901_()) {
                    return this.shouldOpenAt(dcb, context);
                }
                context.temporaryData = null;
                DoorControlBehaviour doorControls = null;
                if (contraption instanceof ElevatorContraption ec) {
                    doorControls = this.getElevatorDoorControl(ec, context);
                }
                if (context.contraption.entity instanceof CarriageContraptionEntity cce) {
                    doorControls = this.getTrainStationDoorControl(cce, context);
                }
                if (doorControls == null) {
                    return false;
                } else {
                    context.temporaryData = new WeakReference(doorControls);
                    return this.shouldOpenAt(doorControls, context);
                }
            }
        }
    }

    protected boolean shouldOpenAt(DoorControlBehaviour controller, MovementContext context) {
        if (controller.mode == DoorControl.ALL) {
            return true;
        } else {
            return controller.mode == DoorControl.NONE ? false : controller.mode.matches(this.getDoorFacing(context));
        }
    }

    protected DoorControlBehaviour getElevatorDoorControl(ElevatorContraption ec, MovementContext context) {
        Integer currentTargetY = ec.getCurrentTargetY(context.world);
        if (currentTargetY == null) {
            return null;
        } else {
            ElevatorColumn.ColumnCoords columnCoords = ec.getGlobalColumn();
            if (columnCoords == null) {
                return null;
            } else {
                ElevatorColumn elevatorColumn = ElevatorColumn.get(context.world, columnCoords);
                return elevatorColumn == null ? null : BlockEntityBehaviour.get(context.world, elevatorColumn.contactAt(currentTargetY), DoorControlBehaviour.TYPE);
            }
        }
    }

    protected DoorControlBehaviour getTrainStationDoorControl(CarriageContraptionEntity cce, MovementContext context) {
        Carriage carriage = cce.getCarriage();
        if (carriage != null && carriage.train != null) {
            GlobalStation currentStation = carriage.train.getCurrentStation();
            if (currentStation == null) {
                return null;
            } else {
                BlockPos stationPos = currentStation.getBlockEntityPos();
                ResourceKey<Level> stationDim = currentStation.getBlockEntityDimension();
                MinecraftServer server = context.world.getServer();
                if (server == null) {
                    return null;
                } else {
                    ServerLevel stationLevel = server.getLevel(stationDim);
                    return stationLevel != null && stationLevel.m_46749_(stationPos) ? BlockEntityBehaviour.get(stationLevel, stationPos, DoorControlBehaviour.TYPE) : null;
                }
            }
        } else {
            return null;
        }
    }

    protected Direction getDoorFacing(MovementContext context) {
        Direction stateFacing = (Direction) context.state.m_61143_(DoorBlock.FACING);
        Direction originalFacing = Direction.get(Direction.AxisDirection.POSITIVE, stateFacing.getAxis());
        Vec3 centerOfContraption = context.contraption.bounds.getCenter();
        Vec3 diff = Vec3.atCenterOf(context.localPos).add(Vec3.atLowerCornerOf(stateFacing.getNormal()).scale(-0.45F)).subtract(centerOfContraption);
        if (originalFacing.getAxis().choose(diff.x, diff.y, diff.z) < 0.0) {
            originalFacing = originalFacing.getOpposite();
        }
        Vec3 directionVec = Vec3.atLowerCornerOf(originalFacing.getNormal());
        directionVec = (Vec3) context.rotation.apply(directionVec);
        return Direction.getNearest(directionVec.x, directionVec.y, directionVec.z);
    }
}