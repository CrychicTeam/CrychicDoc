package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BezierConnection implements Iterable<BezierConnection.Segment> {

    public Couple<BlockPos> tePositions;

    public Couple<Vec3> starts;

    public Couple<Vec3> axes;

    public Couple<Vec3> normals;

    public Couple<Integer> smoothing;

    public boolean primary;

    public boolean hasGirder;

    protected TrackMaterial trackMaterial;

    Vec3 finish1;

    Vec3 finish2;

    private boolean resolved;

    private double length;

    private float[] stepLUT;

    private int segments;

    private double radius;

    private double handleLength;

    private AABB bounds;

    private BezierConnection.SegmentAngles[] bakedSegments;

    private BezierConnection.GirderAngles[] bakedGirders;

    public BezierConnection(Couple<BlockPos> positions, Couple<Vec3> starts, Couple<Vec3> axes, Couple<Vec3> normals, boolean primary, boolean girder, TrackMaterial material) {
        this.tePositions = positions;
        this.starts = starts;
        this.axes = axes;
        this.normals = normals;
        this.primary = primary;
        this.hasGirder = girder;
        this.trackMaterial = material;
        this.resolved = false;
    }

    public BezierConnection secondary() {
        BezierConnection bezierConnection = new BezierConnection(this.tePositions.swap(), this.starts.swap(), this.axes.swap(), this.normals.swap(), !this.primary, this.hasGirder, this.trackMaterial);
        if (this.smoothing != null) {
            bezierConnection.smoothing = this.smoothing.swap();
        }
        return bezierConnection;
    }

    public BezierConnection clone() {
        return this.secondary().secondary();
    }

    private static boolean coupleEquals(Couple<?> a, Couple<?> b) {
        if (a.getFirst().equals(b.getFirst()) && a.getSecond().equals(b.getSecond())) {
            return true;
        } else {
            if (a.getFirst() instanceof Vec3 aFirst && a.getSecond() instanceof Vec3 aSecond && b.getFirst() instanceof Vec3 bFirst && b.getSecond() instanceof Vec3 bSecond && aFirst.closerThan(bFirst, 1.0E-6) && aSecond.closerThan(bSecond, 1.0E-6)) {
                return true;
            }
            return false;
        }
    }

    public boolean equalsSansMaterial(BezierConnection other) {
        return this.equalsSansMaterialInner(other) || this.equalsSansMaterialInner(other.secondary());
    }

    private boolean equalsSansMaterialInner(BezierConnection other) {
        return this == other || other != null && coupleEquals(this.tePositions, other.tePositions) && coupleEquals(this.starts, other.starts) && coupleEquals(this.axes, other.axes) && coupleEquals(this.normals, other.normals) && this.hasGirder == other.hasGirder;
    }

    public BezierConnection(CompoundTag compound, BlockPos localTo) {
        this(Couple.deserializeEach(compound.getList("Positions", 10), NbtUtils::m_129239_).map(b -> b.offset(localTo)), Couple.deserializeEach(compound.getList("Starts", 10), VecHelper::readNBTCompound).map(v -> v.add(Vec3.atLowerCornerOf(localTo))), Couple.deserializeEach(compound.getList("Axes", 10), VecHelper::readNBTCompound), Couple.deserializeEach(compound.getList("Normals", 10), VecHelper::readNBTCompound), compound.getBoolean("Primary"), compound.getBoolean("Girder"), TrackMaterial.deserialize(compound.getString("Material")));
        if (compound.contains("Smoothing")) {
            this.smoothing = Couple.deserializeEach(compound.getList("Smoothing", 10), NBTHelper::intFromCompound);
        }
    }

    public CompoundTag write(BlockPos localTo) {
        Couple<BlockPos> tePositions = this.tePositions.map(b -> b.subtract(localTo));
        Couple<Vec3> starts = this.starts.map(v -> v.subtract(Vec3.atLowerCornerOf(localTo)));
        CompoundTag compound = new CompoundTag();
        compound.putBoolean("Girder", this.hasGirder);
        compound.putBoolean("Primary", this.primary);
        compound.put("Positions", tePositions.serializeEach(NbtUtils::m_129224_));
        compound.put("Starts", starts.serializeEach(VecHelper::writeNBTCompound));
        compound.put("Axes", this.axes.serializeEach(VecHelper::writeNBTCompound));
        compound.put("Normals", this.normals.serializeEach(VecHelper::writeNBTCompound));
        compound.putString("Material", this.getMaterial().id.toString());
        if (this.smoothing != null) {
            compound.put("Smoothing", this.smoothing.serializeEach(NBTHelper::intToCompound));
        }
        return compound;
    }

    public BezierConnection(FriendlyByteBuf buffer) {
        this(Couple.create(buffer::m_130135_), Couple.create(() -> VecHelper.read(buffer)), Couple.create(() -> VecHelper.read(buffer)), Couple.create(() -> VecHelper.read(buffer)), buffer.readBoolean(), buffer.readBoolean(), TrackMaterial.deserialize(buffer.readUtf()));
        if (buffer.readBoolean()) {
            this.smoothing = Couple.create(buffer::m_130242_);
        }
    }

    public void write(FriendlyByteBuf buffer) {
        this.tePositions.forEach(buffer::m_130064_);
        this.starts.forEach(v -> VecHelper.write(v, buffer));
        this.axes.forEach(v -> VecHelper.write(v, buffer));
        this.normals.forEach(v -> VecHelper.write(v, buffer));
        buffer.writeBoolean(this.primary);
        buffer.writeBoolean(this.hasGirder);
        buffer.writeUtf(this.getMaterial().id.toString());
        buffer.writeBoolean(this.smoothing != null);
        if (this.smoothing != null) {
            this.smoothing.forEach(buffer::m_130130_);
        }
    }

    public BlockPos getKey() {
        return this.tePositions.getSecond();
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public int yOffsetAt(Vec3 end) {
        if (this.smoothing == null) {
            return 0;
        } else if (TrackBlockEntityTilt.compareHandles(this.starts.getFirst(), end)) {
            return this.smoothing.getFirst();
        } else {
            return TrackBlockEntityTilt.compareHandles(this.starts.getSecond(), end) ? this.smoothing.getSecond() : 0;
        }
    }

    public double getLength() {
        this.resolve();
        return this.length;
    }

    public float[] getStepLUT() {
        this.resolve();
        return this.stepLUT;
    }

    public int getSegmentCount() {
        this.resolve();
        return this.segments;
    }

    public Vec3 getPosition(double t) {
        this.resolve();
        return VecHelper.bezier(this.starts.getFirst(), this.starts.getSecond(), this.finish1, this.finish2, (float) t);
    }

    public double getRadius() {
        this.resolve();
        return this.radius;
    }

    public double getHandleLength() {
        this.resolve();
        return this.handleLength;
    }

    public float getSegmentT(int index) {
        return index == this.segments ? 1.0F : (float) index * this.stepLUT[index] / (float) this.segments;
    }

    public double incrementT(double currentT, double distance) {
        this.resolve();
        double dx = VecHelper.bezierDerivative(this.starts.getFirst(), this.starts.getSecond(), this.finish1, this.finish2, (float) currentT).length() / this.getLength();
        return currentT + distance / dx;
    }

    public AABB getBounds() {
        this.resolve();
        return this.bounds;
    }

    public Vec3 getNormal(double t) {
        this.resolve();
        Vec3 end1 = this.starts.getFirst();
        Vec3 end2 = this.starts.getSecond();
        Vec3 fn1 = this.normals.getFirst();
        Vec3 fn2 = this.normals.getSecond();
        Vec3 derivative = VecHelper.bezierDerivative(end1, end2, this.finish1, this.finish2, (float) t).normalize();
        Vec3 faceNormal = fn1.equals(fn2) ? fn1 : VecHelper.slerp((float) t, fn1, fn2);
        Vec3 normal = faceNormal.cross(derivative).normalize();
        return derivative.cross(normal);
    }

    private void resolve() {
        if (!this.resolved) {
            this.resolved = true;
            Vec3 end1 = this.starts.getFirst();
            Vec3 end2 = this.starts.getSecond();
            Vec3 axis1 = this.axes.getFirst().normalize();
            Vec3 axis2 = this.axes.getSecond().normalize();
            this.determineHandles(end1, end2, axis1, axis2);
            this.finish1 = axis1.scale(this.handleLength).add(end1);
            this.finish2 = axis2.scale(this.handleLength).add(end2);
            int scanCount = 16;
            this.length = 0.0;
            Vec3 previous = end1;
            for (int i = 0; i <= scanCount; i++) {
                float t = (float) i / (float) scanCount;
                Vec3 result = VecHelper.bezier(end1, end2, this.finish1, this.finish2, t);
                if (previous != null) {
                    this.length = this.length + result.distanceTo(previous);
                }
                previous = result;
            }
            this.segments = (int) (this.length * 2.0);
            this.stepLUT = new float[this.segments + 1];
            this.stepLUT[0] = 1.0F;
            float combinedDistance = 0.0F;
            this.bounds = new AABB(end1, end2);
            Vec3 previousx = end1;
            for (int i = 0; i <= this.segments; i++) {
                float t = (float) i / (float) this.segments;
                Vec3 result = VecHelper.bezier(end1, end2, this.finish1, this.finish2, t);
                this.bounds = this.bounds.minmax(new AABB(result, result));
                if (i > 0) {
                    combinedDistance = (float) ((double) combinedDistance + result.distanceTo(previousx) / this.length);
                    this.stepLUT[i] = t / combinedDistance;
                }
                previousx = result;
            }
            this.bounds = this.bounds.inflate(1.375);
        }
    }

    private void determineHandles(Vec3 end1, Vec3 end2, Vec3 axis1, Vec3 axis2) {
        Vec3 cross1 = axis1.cross(new Vec3(0.0, 1.0, 0.0));
        Vec3 cross2 = axis2.cross(new Vec3(0.0, 1.0, 0.0));
        this.radius = 0.0;
        double a1 = Mth.atan2(-axis2.z, -axis2.x);
        double a2 = Mth.atan2(axis1.z, axis1.x);
        double angle = a1 - a2;
        float circle = (float) (Math.PI * 2);
        angle = (angle + (double) circle) % (double) circle;
        if (Math.abs((double) circle - angle) < Math.abs(angle)) {
            angle = (double) circle - angle;
        }
        if (Mth.equal(angle, 0.0)) {
            double[] intersect = VecHelper.intersect(end1, end2, axis1, cross2, Direction.Axis.Y);
            if (intersect != null) {
                double t = Math.abs(intersect[0]);
                double u = Math.abs(intersect[1]);
                double min = Math.min(t, u);
                double max = Math.max(t, u);
                if (min > 1.2 && max / min > 1.0 && max / min < 3.0) {
                    this.handleLength = max - min;
                    return;
                }
            }
            this.handleLength = end2.distanceTo(end1) / 3.0;
        } else {
            double n = (double) circle / angle;
            double factor = 1.3333333333333333 * Math.tan(Math.PI / (2.0 * n));
            double[] intersect = VecHelper.intersect(end1, end2, cross1, cross2, Direction.Axis.Y);
            if (intersect == null) {
                this.handleLength = end2.distanceTo(end1) / 3.0;
            } else {
                this.radius = Math.abs(intersect[1]);
                this.handleLength = this.radius * factor;
                if (Mth.equal(this.handleLength, 0.0)) {
                    this.handleLength = 1.0;
                }
            }
        }
    }

    public Iterator<BezierConnection.Segment> iterator() {
        this.resolve();
        Vec3 offset = Vec3.atLowerCornerOf(this.tePositions.getFirst()).scale(-1.0).add(0.0, 0.1875, 0.0);
        return new BezierConnection.Bezierator(this, offset);
    }

    public void addItemsToPlayer(Player player) {
        Inventory inv = player.getInventory();
        for (int tracks = this.getTrackItemCost(); tracks > 0; tracks -= 64) {
            inv.placeItemBackInInventory(new ItemStack(this.getMaterial().getBlock(), Math.min(64, tracks)));
        }
        for (int girders = this.getGirderItemCost(); girders > 0; girders -= 64) {
            inv.placeItemBackInInventory(AllBlocks.METAL_GIRDER.asStack(Math.min(64, girders)));
        }
    }

    public int getGirderItemCost() {
        return this.hasGirder ? this.getTrackItemCost() * 2 : 0;
    }

    public int getTrackItemCost() {
        return (this.getSegmentCount() + 1) / 2;
    }

    public void spawnItems(Level level) {
        if (level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            Vec3 origin = Vec3.atLowerCornerOf(this.tePositions.getFirst());
            for (BezierConnection.Segment segment : this) {
                if (segment.index % 2 == 0 && segment.index != this.getSegmentCount()) {
                    Vec3 v = VecHelper.offsetRandomly(segment.position, level.random, 0.125F).add(origin);
                    ItemEntity entity = new ItemEntity(level, v.x, v.y, v.z, this.getMaterial().asStack());
                    entity.setDefaultPickUpDelay();
                    level.m_7967_(entity);
                    if (this.hasGirder) {
                        for (int i = 0; i < 2; i++) {
                            entity = new ItemEntity(level, v.x, v.y, v.z, AllBlocks.METAL_GIRDER.asStack());
                            entity.setDefaultPickUpDelay();
                            level.m_7967_(entity);
                        }
                    }
                }
            }
        }
    }

    public void spawnDestroyParticles(Level level) {
        BlockParticleOption data = new BlockParticleOption(ParticleTypes.BLOCK, this.getMaterial().getBlock().m_49966_());
        BlockParticleOption girderData = new BlockParticleOption(ParticleTypes.BLOCK, AllBlocks.METAL_GIRDER.getDefaultState());
        if (level instanceof ServerLevel slevel) {
            Vec3 origin = Vec3.atLowerCornerOf(this.tePositions.getFirst());
            for (BezierConnection.Segment segment : this) {
                for (int offset : Iterate.positiveAndNegative) {
                    Vec3 v = segment.position.add(segment.normal.scale((double) (0.875F * (float) offset))).add(origin);
                    slevel.sendParticles(data, v.x, v.y, v.z, 1, 0.0, 0.0, 0.0, 0.0);
                    if (this.hasGirder) {
                        slevel.sendParticles(girderData, v.x, v.y - 0.5, v.z, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    public TrackMaterial getMaterial() {
        return this.trackMaterial;
    }

    public void setMaterial(TrackMaterial material) {
        this.trackMaterial = material;
    }

    @OnlyIn(Dist.CLIENT)
    public BezierConnection.SegmentAngles[] getBakedSegments() {
        if (this.bakedSegments != null) {
            return this.bakedSegments;
        } else {
            int segmentCount = this.getSegmentCount();
            this.bakedSegments = new BezierConnection.SegmentAngles[segmentCount + 1];
            Couple<Vec3> previousOffsets = null;
            for (BezierConnection.Segment segment : this) {
                int i = segment.index;
                boolean end = i == 0 || i == segmentCount;
                BezierConnection.SegmentAngles angles = this.bakedSegments[i] = new BezierConnection.SegmentAngles();
                Couple<Vec3> railOffsets = Couple.create(segment.position.add(segment.normal.scale(0.965F)), segment.position.subtract(segment.normal.scale(0.965F)));
                Vec3 railMiddle = railOffsets.getFirst().add(railOffsets.getSecond()).scale(0.5);
                if (previousOffsets == null) {
                    previousOffsets = railOffsets;
                } else {
                    Vec3 prevMiddle = previousOffsets.getFirst().add(previousOffsets.getSecond()).scale(0.5);
                    Vec3 tieAngles = TrackRenderer.getModelAngles(segment.normal, railMiddle.subtract(prevMiddle));
                    angles.lightPosition = BlockPos.containing(railMiddle);
                    angles.railTransforms = Couple.create(null, null);
                    PoseStack poseStack = new PoseStack();
                    ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).translate(prevMiddle)).rotateYRadians(tieAngles.y)).rotateXRadians(tieAngles.x)).rotateZRadians(tieAngles.z)).translate(-0.5, -0.12890625, 0.0);
                    angles.tieTransform = poseStack.last();
                    float scale = end ? 2.2F : 2.1F;
                    for (boolean first : Iterate.trueAndFalse) {
                        Vec3 railI = railOffsets.get(first);
                        Vec3 prevI = previousOffsets.get(first);
                        Vec3 diff = railI.subtract(prevI);
                        Vec3 anglesI = TrackRenderer.getModelAngles(segment.normal, diff);
                        poseStack = new PoseStack();
                        ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).translate(prevI)).rotateYRadians(anglesI.y)).rotateXRadians(anglesI.x)).rotateZRadians(anglesI.z)).translate(0.0, -0.12890625, -0.03125)).scale(1.0F, 1.0F, (float) diff.length() * scale);
                        angles.railTransforms.set(first, poseStack.last());
                    }
                    previousOffsets = railOffsets;
                }
            }
            return this.bakedSegments;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public BezierConnection.GirderAngles[] getBakedGirders() {
        if (this.bakedGirders != null) {
            return this.bakedGirders;
        } else {
            int segmentCount = this.getSegmentCount();
            this.bakedGirders = new BezierConnection.GirderAngles[segmentCount + 1];
            Couple<Couple<Vec3>> previousOffsets = null;
            for (BezierConnection.Segment segment : this) {
                int i = segment.index;
                boolean end = i == 0 || i == segmentCount;
                BezierConnection.GirderAngles angles = this.bakedGirders[i] = new BezierConnection.GirderAngles();
                Vec3 leftGirder = segment.position.add(segment.normal.scale(0.965F));
                Vec3 rightGirder = segment.position.subtract(segment.normal.scale(0.965F));
                Vec3 upNormal = segment.derivative.normalize().cross(segment.normal);
                Vec3 firstGirderOffset = upNormal.scale(-0.5);
                Vec3 secondGirderOffset = upNormal.scale(-0.625);
                Vec3 leftTop = segment.position.add(segment.normal.scale(1.0)).add(firstGirderOffset);
                Vec3 rightTop = segment.position.subtract(segment.normal.scale(1.0)).add(firstGirderOffset);
                Vec3 leftBottom = leftTop.add(secondGirderOffset);
                Vec3 rightBottom = rightTop.add(secondGirderOffset);
                angles.lightPosition = BlockPos.containing(leftGirder.add(rightGirder).scale(0.5));
                Couple<Couple<Vec3>> offsets = Couple.create(Couple.create(leftTop, rightTop), Couple.create(leftBottom, rightBottom));
                if (previousOffsets == null) {
                    previousOffsets = offsets;
                } else {
                    angles.beams = Couple.create(null, null);
                    angles.beamCaps = Couple.create(Couple.create(null, null), Couple.create(null, null));
                    float scale = end ? 2.3F : 2.2F;
                    for (boolean first : Iterate.trueAndFalse) {
                        Vec3 currentBeam = offsets.getFirst().get(first).add(offsets.getSecond().get(first)).scale(0.5);
                        Vec3 previousBeam = previousOffsets.getFirst().get(first).add(previousOffsets.getSecond().get(first)).scale(0.5);
                        Vec3 beamDiff = currentBeam.subtract(previousBeam);
                        Vec3 beamAngles = TrackRenderer.getModelAngles(segment.normal, beamDiff);
                        PoseStack poseStack = new PoseStack();
                        ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).translate(previousBeam)).rotateYRadians(beamAngles.y)).rotateXRadians(beamAngles.x)).rotateZRadians(beamAngles.z)).translate(0.0, (double) (0.125F + (float) (segment.index % 2 == 0 ? 1 : -1) / 2048.0F - 9.765625E-4F), -0.03125)).scale(1.0F, 1.0F, (float) beamDiff.length() * scale);
                        angles.beams.set(first, poseStack.last());
                        for (boolean top : Iterate.trueAndFalse) {
                            Vec3 current = offsets.get(top).get(first);
                            Vec3 previous = previousOffsets.get(top).get(first);
                            Vec3 diff = current.subtract(previous);
                            Vec3 capAngles = TrackRenderer.getModelAngles(segment.normal, diff);
                            poseStack = new PoseStack();
                            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).translate(previous)).rotateYRadians(capAngles.y)).rotateXRadians(capAngles.x)).rotateZRadians(capAngles.z)).translate(0.0, (double) (0.125F + (float) (segment.index % 2 == 0 ? 1 : -1) / 2048.0F - 9.765625E-4F), -0.03125)).rotateZ(top ? 0.0 : 0.0)).scale(1.0F, 1.0F, (float) diff.length() * scale);
                            angles.beamCaps.get(top).set(first, poseStack.last());
                        }
                    }
                    previousOffsets = offsets;
                }
            }
            return this.bakedGirders;
        }
    }

    private static class Bezierator implements Iterator<BezierConnection.Segment> {

        private final BezierConnection bc;

        private final BezierConnection.Segment segment;

        private final Vec3 end1;

        private final Vec3 end2;

        private final Vec3 finish1;

        private final Vec3 finish2;

        private final Vec3 faceNormal1;

        private final Vec3 faceNormal2;

        private Bezierator(BezierConnection bc, Vec3 offset) {
            bc.resolve();
            this.bc = bc;
            this.end1 = bc.starts.getFirst().add(offset);
            this.end2 = bc.starts.getSecond().add(offset);
            this.finish1 = bc.axes.getFirst().scale(bc.handleLength).add(this.end1);
            this.finish2 = bc.axes.getSecond().scale(bc.handleLength).add(this.end2);
            this.faceNormal1 = bc.normals.getFirst();
            this.faceNormal2 = bc.normals.getSecond();
            this.segment = new BezierConnection.Segment();
            this.segment.index = -1;
        }

        public boolean hasNext() {
            return this.segment.index + 1 <= this.bc.segments;
        }

        public BezierConnection.Segment next() {
            this.segment.index++;
            float t = this.bc.getSegmentT(this.segment.index);
            this.segment.position = VecHelper.bezier(this.end1, this.end2, this.finish1, this.finish2, t);
            this.segment.derivative = VecHelper.bezierDerivative(this.end1, this.end2, this.finish1, this.finish2, t).normalize();
            this.segment.faceNormal = this.faceNormal1.equals(this.faceNormal2) ? this.faceNormal1 : VecHelper.slerp(t, this.faceNormal1, this.faceNormal2);
            this.segment.normal = this.segment.faceNormal.cross(this.segment.derivative).normalize();
            return this.segment;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GirderAngles {

        public Couple<PoseStack.Pose> beams;

        public Couple<Couple<PoseStack.Pose>> beamCaps;

        public BlockPos lightPosition;
    }

    public static class Segment {

        public int index;

        public Vec3 position;

        public Vec3 derivative;

        public Vec3 faceNormal;

        public Vec3 normal;
    }

    @OnlyIn(Dist.CLIENT)
    public static class SegmentAngles {

        public PoseStack.Pose tieTransform;

        public Couple<PoseStack.Pose> railTransforms;

        public BlockPos lightPosition;
    }
}