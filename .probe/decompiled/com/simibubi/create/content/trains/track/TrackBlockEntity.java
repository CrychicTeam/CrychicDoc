package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.blockEntity.IMergeableBE;
import com.simibubi.create.foundation.blockEntity.RemoveBlockEntityPacket;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.DistExecutor;

public class TrackBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity, IMergeableBE {

    Map<BlockPos, BezierConnection> connections = new HashMap();

    boolean cancelDrops;

    public Pair<ResourceKey<Level>, BlockPos> boundLocation;

    public TrackBlockEntityTilt tilt;

    public TrackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(100);
        this.tilt = new TrackBlockEntityTilt(this);
    }

    public Map<BlockPos, BezierConnection> getConnections() {
        return this.connections;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.f_58857_.isClientSide && this.hasInteractableConnections()) {
            this.registerToCurveInteraction();
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tilt.undoSmoothing();
    }

    @Override
    public void lazyTick() {
        for (BezierConnection connection : this.connections.values()) {
            if (connection.isPrimary()) {
                this.manageFakeTracksAlong(connection, false);
            }
        }
    }

    public void validateConnections() {
        Set<BlockPos> invalid = new HashSet();
        for (Entry<BlockPos, BezierConnection> entry : this.connections.entrySet()) {
            BlockPos key = (BlockPos) entry.getKey();
            BezierConnection bc = (BezierConnection) entry.getValue();
            if (key.equals(bc.getKey()) && this.f_58858_.equals(bc.tePositions.getFirst())) {
                BlockState blockState = this.f_58857_.getBlockState(key);
                if (blockState.m_60734_() instanceof ITrackBlock trackBlock && !(Boolean) blockState.m_61143_(TrackBlock.HAS_BE)) {
                    for (Vec3 v : trackBlock.getTrackAxes(this.f_58857_, key, blockState)) {
                        Vec3 bcEndAxis = bc.axes.getSecond();
                        if (v.distanceTo(bcEndAxis) < 9.765625E-4 || v.distanceTo(bcEndAxis.scale(-1.0)) < 9.765625E-4) {
                            this.f_58857_.setBlock(key, (BlockState) blockState.m_61124_(TrackBlock.HAS_BE, true), 3);
                        }
                    }
                }
                BlockEntity blockEntity = this.f_58857_.getBlockEntity(key);
                if (!(blockEntity instanceof TrackBlockEntity trackBE) || blockEntity.isRemoved()) {
                    invalid.add(key);
                    continue;
                }
                if (!trackBE.connections.containsKey(this.f_58858_)) {
                    trackBE.addConnection(bc.secondary());
                    trackBE.tilt.tryApplySmoothing();
                }
            } else {
                invalid.add(key);
            }
        }
        for (BlockPos blockPos : invalid) {
            this.removeConnection(blockPos);
        }
    }

    public void addConnection(BezierConnection connection) {
        if (!this.connections.containsKey(connection.getKey()) || !connection.equalsSansMaterial((BezierConnection) this.connections.get(connection.getKey()))) {
            this.connections.put(connection.getKey(), connection);
            this.f_58857_.m_186460_(this.f_58858_, this.m_58900_().m_60734_(), 1);
            this.notifyUpdate();
            if (connection.isPrimary()) {
                this.manageFakeTracksAlong(connection, false);
            }
        }
    }

    public void removeConnection(BlockPos target) {
        if (this.isTilted()) {
            this.tilt.captureSmoothingHandles();
        }
        BezierConnection removed = (BezierConnection) this.connections.remove(target);
        this.notifyUpdate();
        if (removed != null) {
            this.manageFakeTracksAlong(removed, true);
        }
        if (this.connections.isEmpty() && !((TrackShape) this.m_58900_().m_61145_(TrackBlock.SHAPE).orElse(TrackShape.NONE)).isPortal()) {
            BlockState blockState = this.f_58857_.getBlockState(this.f_58858_);
            if (blockState.m_61138_(TrackBlock.HAS_BE)) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) blockState.m_61124_(TrackBlock.HAS_BE, false));
            }
            AllPackets.getChannel().send(this.packetTarget(), new RemoveBlockEntityPacket(this.f_58858_));
        }
    }

    public void removeInboundConnections(boolean dropAndDiscard) {
        for (BezierConnection bezierConnection : this.connections.values()) {
            if (!(this.f_58857_.getBlockEntity(bezierConnection.getKey()) instanceof TrackBlockEntity tbe)) {
                return;
            }
            tbe.removeConnection(bezierConnection.tePositions.getFirst());
            if (dropAndDiscard) {
                if (!this.cancelDrops) {
                    bezierConnection.spawnItems(this.f_58857_);
                }
                bezierConnection.spawnDestroyParticles(this.f_58857_);
            }
        }
        if (dropAndDiscard) {
            AllPackets.getChannel().send(this.packetTarget(), new RemoveBlockEntityPacket(this.f_58858_));
        }
    }

    public void bind(ResourceKey<Level> boundDimension, BlockPos boundLocation) {
        this.boundLocation = Pair.of(boundDimension, boundLocation);
        this.m_6596_();
    }

    public boolean isTilted() {
        return this.tilt.smoothingAngle.isPresent();
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        this.writeTurns(tag, true);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        this.writeTurns(tag, false);
        if (this.isTilted()) {
            tag.putDouble("Smoothing", (Double) this.tilt.smoothingAngle.get());
        }
        if (this.boundLocation != null) {
            tag.put("BoundLocation", NbtUtils.writeBlockPos(this.boundLocation.getSecond()));
            tag.putString("BoundDimension", this.boundLocation.getFirst().location().toString());
        }
    }

    private void writeTurns(CompoundTag tag, boolean restored) {
        ListTag listTag = new ListTag();
        for (BezierConnection bezierConnection : this.connections.values()) {
            listTag.add((restored ? this.tilt.restoreToOriginalCurve(bezierConnection.clone()) : bezierConnection).write(this.f_58858_));
        }
        tag.put("Connections", listTag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.connections.clear();
        for (Tag t : tag.getList("Connections", 10)) {
            if (!(t instanceof CompoundTag)) {
                return;
            }
            BezierConnection connection = new BezierConnection((CompoundTag) t, this.f_58858_);
            this.connections.put(connection.getKey(), connection);
        }
        boolean smoothingPreviously = this.tilt.smoothingAngle.isPresent();
        this.tilt.smoothingAngle = Optional.ofNullable(tag.contains("Smoothing") ? tag.getDouble("Smoothing") : null);
        if (smoothingPreviously != this.tilt.smoothingAngle.isPresent() && clientPacket) {
            this.requestModelDataUpdate();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 16);
        }
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
        if (this.hasInteractableConnections()) {
            this.registerToCurveInteraction();
        } else {
            this.removeFromCurveInteraction();
        }
        if (tag.contains("BoundLocation")) {
            this.boundLocation = Pair.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("BoundDimension"))), NbtUtils.readBlockPos(tag.getCompound("BoundLocation")));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void accept(BlockEntity other) {
        if (other instanceof TrackBlockEntity track) {
            this.connections.putAll(track.connections);
        }
        this.validateConnections();
        this.f_58857_.m_186460_(this.f_58858_, this.m_58900_().m_60734_(), 1);
    }

    public boolean hasInteractableConnections() {
        for (BezierConnection connection : this.connections.values()) {
            if (connection.isPrimary()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void transform(StructureTransform transform) {
        Map<BlockPos, BezierConnection> restoredConnections = new HashMap();
        for (Entry<BlockPos, BezierConnection> entry : this.connections.entrySet()) {
            restoredConnections.put((BlockPos) entry.getKey(), this.tilt.restoreToOriginalCurve(this.tilt.restoreToOriginalCurve(((BezierConnection) entry.getValue()).secondary()).secondary()));
        }
        this.connections = restoredConnections;
        this.tilt.smoothingAngle = Optional.empty();
        if (transform.rotationAxis == Direction.Axis.Y) {
            Map<BlockPos, BezierConnection> transformedConnections = new HashMap();
            for (Entry<BlockPos, BezierConnection> entry : this.connections.entrySet()) {
                BezierConnection newConnection = (BezierConnection) entry.getValue();
                newConnection.normals.replace(transform::applyWithoutOffsetUncentered);
                newConnection.axes.replace(transform::applyWithoutOffsetUncentered);
                BlockPos diff = newConnection.tePositions.getSecond().subtract(newConnection.tePositions.getFirst());
                newConnection.tePositions.setSecond(BlockPos.containing(Vec3.atCenterOf(newConnection.tePositions.getFirst()).add(transform.applyWithoutOffsetUncentered(Vec3.atLowerCornerOf(diff)))));
                Vec3 beVec = Vec3.atLowerCornerOf(this.f_58858_);
                Vec3 teCenterVec = beVec.add(0.5, 0.5, 0.5);
                Vec3 start = newConnection.starts.getFirst();
                Vec3 startToBE = start.subtract(teCenterVec);
                Vec3 endToStart = newConnection.starts.getSecond().subtract(start);
                startToBE = transform.applyWithoutOffsetUncentered(startToBE).add(teCenterVec);
                endToStart = transform.applyWithoutOffsetUncentered(endToStart).add(startToBE);
                newConnection.starts.setFirst(new TrackNodeLocation(startToBE).getLocation());
                newConnection.starts.setSecond(new TrackNodeLocation(endToStart).getLocation());
                BlockPos newTarget = newConnection.getKey();
                transformedConnections.put(newTarget, newConnection);
            }
            this.connections = transformedConnections;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (this.f_58857_.isClientSide) {
            this.removeFromCurveInteraction();
        }
    }

    @Override
    public void remove() {
        super.remove();
        for (BezierConnection connection : this.connections.values()) {
            this.manageFakeTracksAlong(connection, true);
        }
        if (this.boundLocation != null && this.f_58857_ instanceof ServerLevel) {
            ServerLevel otherLevel = this.f_58857_.getServer().getLevel(this.boundLocation.getFirst());
            if (otherLevel == null) {
                return;
            }
            if (AllTags.AllBlockTags.TRACKS.matches(otherLevel.m_8055_(this.boundLocation.getSecond()))) {
                otherLevel.m_46961_(this.boundLocation.getSecond(), false);
            }
        }
    }

    private void registerToCurveInteraction() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::registerToCurveInteractionUnsafe);
    }

    private void removeFromCurveInteraction() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::removeFromCurveInteractionUnsafe);
    }

    public ModelData getModelData() {
        return !this.isTilted() ? super.getModelData() : ModelData.builder().with(TrackBlockEntityTilt.ASCENDING_PROPERTY, (Double) this.tilt.smoothingAngle.get()).build();
    }

    @OnlyIn(Dist.CLIENT)
    private void registerToCurveInteractionUnsafe() {
        TrackBlockOutline.TRACKS_WITH_TURNS.get(this.f_58857_).put(this.f_58858_, this);
    }

    @OnlyIn(Dist.CLIENT)
    private void removeFromCurveInteractionUnsafe() {
        TrackBlockOutline.TRACKS_WITH_TURNS.get(this.f_58857_).remove(this.f_58858_);
    }

    public void manageFakeTracksAlong(BezierConnection bc, boolean remove) {
        Map<Pair<Integer, Integer>, Double> yLevels = new HashMap();
        BlockPos tePosition = bc.tePositions.getFirst();
        Vec3 end1 = bc.starts.getFirst().subtract(Vec3.atLowerCornerOf(tePosition)).add(0.0, 0.1875, 0.0);
        Vec3 end2 = bc.starts.getSecond().subtract(Vec3.atLowerCornerOf(tePosition)).add(0.0, 0.1875, 0.0);
        Vec3 axis1 = bc.axes.getFirst();
        Vec3 axis2 = bc.axes.getSecond();
        double handleLength = bc.getHandleLength();
        Vec3 finish1 = axis1.scale(handleLength).add(end1);
        Vec3 finish2 = axis2.scale(handleLength).add(end2);
        Vec3 faceNormal1 = bc.normals.getFirst();
        Vec3 faceNormal2 = bc.normals.getSecond();
        int segCount = bc.getSegmentCount();
        float[] lut = bc.getStepLUT();
        for (int i = 0; i < segCount; i++) {
            float t = i == segCount ? 1.0F : (float) i * lut[i] / (float) segCount;
            t += 0.5F / (float) segCount;
            Vec3 result = VecHelper.bezier(end1, end2, finish1, finish2, t);
            Vec3 derivative = VecHelper.bezierDerivative(end1, end2, finish1, finish2, t).normalize();
            Vec3 faceNormal = faceNormal1.equals(faceNormal2) ? faceNormal1 : VecHelper.slerp(t, faceNormal1, faceNormal2);
            Vec3 normal = faceNormal.cross(derivative).normalize();
            Vec3 below = result.add(faceNormal.scale(-0.25));
            Vec3 rail1 = below.add(normal.scale(0.05F));
            Vec3 rail2 = below.subtract(normal.scale(0.05F));
            Vec3 railMiddle = rail1.add(rail2).scale(0.5);
            for (Vec3 vec : new Vec3[] { railMiddle }) {
                BlockPos pos = BlockPos.containing(vec);
                Pair<Integer, Integer> key = Pair.of(pos.m_123341_(), pos.m_123343_());
                if (!yLevels.containsKey(key) || (Double) yLevels.get(key) > vec.y) {
                    yLevels.put(key, vec.y);
                }
            }
        }
        for (Entry<Pair<Integer, Integer>, Double> entry : yLevels.entrySet()) {
            double yValue = (Double) entry.getValue();
            int floor = Mth.floor(yValue);
            BlockPos targetPos = new BlockPos((Integer) ((Pair) entry.getKey()).getFirst(), floor, (Integer) ((Pair) entry.getKey()).getSecond());
            targetPos = targetPos.offset(tePosition).above(1);
            BlockState stateAtPos = this.f_58857_.getBlockState(targetPos);
            boolean present = AllBlocks.FAKE_TRACK.has(stateAtPos);
            if (remove) {
                if (present) {
                    this.f_58857_.removeBlock(targetPos, false);
                }
            } else {
                FluidState fluidState = stateAtPos.m_60819_();
                if (fluidState.isEmpty() || fluidState.isSourceOfType(Fluids.WATER)) {
                    if (!present && stateAtPos.m_247087_()) {
                        this.f_58857_.setBlock(targetPos, ProperWaterloggedBlock.withWater(this.f_58857_, AllBlocks.FAKE_TRACK.getDefaultState(), targetPos), 3);
                    }
                    FakeTrackBlock.keepAlive(this.f_58857_, targetPos);
                }
            }
        }
    }
}