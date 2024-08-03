package com.simibubi.create.content.contraptions.actors.psi;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.Optional;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class PortableStorageInterfaceMovement implements MovementBehaviour {

    static final String _workingPos_ = "WorkingPos";

    static final String _clientPrevPos_ = "ClientPrevPos";

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(PortableStorageInterfaceBlock.f_52588_)).getNormal()).scale(1.85F);
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new PSIActorInstance(materialManager, simulationWorld, context);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            PortableStorageInterfaceRenderer.renderInContraption(context, renderWorld, matrices, buffer);
        }
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        boolean onCarriage = context.contraption instanceof CarriageContraption;
        if (!onCarriage || !(context.motion.length() > 0.25)) {
            if (!this.findInterface(context, pos)) {
                context.data.remove("WorkingPos");
            }
        }
    }

    @Override
    public void tick(MovementContext context) {
        if (context.world.isClientSide) {
            getAnimation(context).tickChaser();
        }
        boolean onCarriage = context.contraption instanceof CarriageContraption;
        if (!onCarriage || !(context.motion.length() > 0.25)) {
            if (context.world.isClientSide) {
                BlockPos pos = BlockPos.containing(context.position);
                if (!this.findInterface(context, pos)) {
                    this.reset(context);
                }
            } else if (context.data.contains("WorkingPos")) {
                BlockPos pos = NbtUtils.readBlockPos(context.data.getCompound("WorkingPos"));
                Vec3 target = VecHelper.getCenterOf(pos);
                if (!context.stall && !onCarriage && context.position.closerThan(target, target.distanceTo(context.position.add(context.motion)))) {
                    context.stall = true;
                }
                Optional<Direction> currentFacingIfValid = this.getCurrentFacingIfValid(context);
                if (currentFacingIfValid.isPresent()) {
                    PortableStorageInterfaceBlockEntity stationaryInterface = this.getStationaryInterfaceAt(context.world, pos, context.state, (Direction) currentFacingIfValid.get());
                    if (stationaryInterface == null) {
                        this.reset(context);
                    } else {
                        if (stationaryInterface.connectedEntity == null) {
                            stationaryInterface.startTransferringTo(context.contraption, stationaryInterface.distance);
                        }
                        boolean timerBelow = stationaryInterface.transferTimer <= 4;
                        stationaryInterface.keepAlive = 2;
                        if (context.stall && timerBelow) {
                            context.stall = false;
                        }
                    }
                }
            }
        }
    }

    protected boolean findInterface(MovementContext context, BlockPos pos) {
        if (context.contraption instanceof CarriageContraption cc && !cc.notInPortal()) {
            return false;
        }
        Optional<Direction> currentFacingIfValid = this.getCurrentFacingIfValid(context);
        if (!currentFacingIfValid.isPresent()) {
            return false;
        } else {
            Direction currentFacing = (Direction) currentFacingIfValid.get();
            PortableStorageInterfaceBlockEntity psi = this.findStationaryInterface(context.world, pos, context.state, currentFacing);
            if (psi == null) {
                return false;
            } else if (psi.isPowered()) {
                return false;
            } else {
                context.data.put("WorkingPos", NbtUtils.writeBlockPos(psi.m_58899_()));
                if (!context.world.isClientSide) {
                    Vec3 diff = VecHelper.getCenterOf(psi.m_58899_()).subtract(context.position);
                    diff = VecHelper.project(diff, Vec3.atLowerCornerOf(currentFacing.getNormal()));
                    float distance = (float) (diff.length() + 1.85F - 1.0);
                    psi.startTransferringTo(context.contraption, distance);
                } else {
                    context.data.put("ClientPrevPos", NbtUtils.writeBlockPos(pos));
                    if (context.contraption instanceof CarriageContraption || context.contraption.entity.isStalled() || context.motion.lengthSqr() == 0.0) {
                        getAnimation(context).chase((double) (psi.getConnectionDistance() / 2.0F), 0.25, LerpedFloat.Chaser.LINEAR);
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
    }

    @Override
    public void cancelStall(MovementContext context) {
        this.reset(context);
    }

    public void reset(MovementContext context) {
        context.data.remove("ClientPrevPos");
        context.data.remove("WorkingPos");
        context.stall = false;
        getAnimation(context).chase(0.0, 0.25, LerpedFloat.Chaser.LINEAR);
    }

    private PortableStorageInterfaceBlockEntity findStationaryInterface(Level world, BlockPos pos, BlockState state, Direction facing) {
        for (int i = 0; i < 2; i++) {
            PortableStorageInterfaceBlockEntity interfaceAt = this.getStationaryInterfaceAt(world, pos.relative(facing, i), state, facing);
            if (interfaceAt != null) {
                return interfaceAt;
            }
        }
        return null;
    }

    private PortableStorageInterfaceBlockEntity getStationaryInterfaceAt(Level world, BlockPos pos, BlockState state, Direction facing) {
        if (world.getBlockEntity(pos) instanceof PortableStorageInterfaceBlockEntity psi) {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.m_60734_() != state.m_60734_()) {
                return null;
            } else if (blockState.m_61143_(PortableStorageInterfaceBlock.f_52588_) != facing.getOpposite()) {
                return null;
            } else {
                return psi.isPowered() ? null : psi;
            }
        } else {
            return null;
        }
    }

    private Optional<Direction> getCurrentFacingIfValid(MovementContext context) {
        Vec3 directionVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(PortableStorageInterfaceBlock.f_52588_)).getNormal());
        directionVec = (Vec3) context.rotation.apply(directionVec);
        Direction facingFromVector = Direction.getNearest(directionVec.x, directionVec.y, directionVec.z);
        return directionVec.distanceTo(Vec3.atLowerCornerOf(facingFromVector.getNormal())) > 0.5 ? Optional.empty() : Optional.of(facingFromVector);
    }

    public static LerpedFloat getAnimation(MovementContext context) {
        LerpedFloat nlf = (LerpedFloat) context.temporaryData;
        if (nlf instanceof LerpedFloat) {
            return nlf;
        } else {
            nlf = LerpedFloat.linear();
            context.temporaryData = nlf;
            return nlf;
        }
    }
}