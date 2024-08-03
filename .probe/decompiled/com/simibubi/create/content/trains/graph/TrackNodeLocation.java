package com.simibubi.create.content.trains.graph;

import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrackNodeLocation extends Vec3i {

    public ResourceKey<Level> dimension;

    public int yOffsetPixels;

    public TrackNodeLocation(Vec3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public TrackNodeLocation(double x, double y, double z) {
        super(Mth.floor((float) Math.round(x * 2.0)), Mth.floor(y) * 2, Mth.floor((float) Math.round(z * 2.0)));
    }

    public TrackNodeLocation in(Level level) {
        return this.in(level.dimension());
    }

    public TrackNodeLocation in(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        return this;
    }

    private static TrackNodeLocation fromPackedPos(BlockPos bufferPos) {
        return new TrackNodeLocation(bufferPos);
    }

    private TrackNodeLocation(BlockPos readBlockPos) {
        super(readBlockPos.m_123341_(), readBlockPos.m_123342_(), readBlockPos.m_123343_());
    }

    public Vec3 getLocation() {
        return new Vec3((double) this.m_123341_() / 2.0, (double) this.m_123342_() / 2.0 + (double) this.yOffsetPixels / 16.0, (double) this.m_123343_() / 2.0);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Override
    public boolean equals(Object pOther) {
        if (this.equalsIgnoreDim(pOther) && pOther instanceof TrackNodeLocation tnl && Objects.equals(tnl.dimension, this.dimension)) {
            return true;
        }
        return false;
    }

    public boolean equalsIgnoreDim(Object pOther) {
        if (super.equals(pOther) && pOther instanceof TrackNodeLocation tnl && tnl.yOffsetPixels == this.yOffsetPixels) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.m_123342_() + ((this.m_123343_() + this.yOffsetPixels * 31) * 31 + this.dimension.hashCode()) * 31) * 31 + this.m_123341_();
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag c = NbtUtils.writeBlockPos(new BlockPos(this));
        if (dimensions != null) {
            c.putInt("D", dimensions.encode(this.dimension));
        }
        if (this.yOffsetPixels != 0) {
            c.putInt("YO", this.yOffsetPixels);
        }
        return c;
    }

    public static TrackNodeLocation read(CompoundTag tag, DimensionPalette dimensions) {
        TrackNodeLocation location = fromPackedPos(NbtUtils.readBlockPos(tag));
        if (dimensions != null) {
            location.dimension = dimensions.decode(tag.getInt("D"));
        }
        location.yOffsetPixels = tag.getInt("YO");
        return location;
    }

    public void send(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        buffer.writeVarInt(this.m_123341_());
        buffer.writeShort(this.m_123342_());
        buffer.writeVarInt(this.m_123343_());
        buffer.writeVarInt(this.yOffsetPixels);
        buffer.writeVarInt(dimensions.encode(this.dimension));
    }

    public static TrackNodeLocation receive(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        TrackNodeLocation location = fromPackedPos(new BlockPos(buffer.readVarInt(), buffer.readShort(), buffer.readVarInt()));
        location.yOffsetPixels = buffer.readVarInt();
        location.dimension = dimensions.decode(buffer.readVarInt());
        return location;
    }

    public Collection<BlockPos> allAdjacent() {
        Set<BlockPos> set = new HashSet();
        Vec3 vec3 = this.getLocation().subtract(0.0, (double) this.yOffsetPixels / 16.0, 0.0);
        double step = 0.125;
        for (int x : Iterate.positiveAndNegative) {
            for (int y : Iterate.positiveAndNegative) {
                for (int z : Iterate.positiveAndNegative) {
                    set.add(BlockPos.containing(vec3.add((double) x * step, (double) y * step, (double) z * step)));
                }
            }
        }
        return set;
    }

    public static class DiscoveredLocation extends TrackNodeLocation {

        BezierConnection turn = null;

        boolean forceNode = false;

        Vec3 direction;

        Vec3 normal;

        TrackMaterial materialA;

        TrackMaterial materialB;

        public DiscoveredLocation(Level level, double x, double y, double z) {
            super(x, y, z);
            this.in(level);
        }

        public DiscoveredLocation(ResourceKey<Level> dimension, Vec3 vec) {
            super(vec);
            this.in(dimension);
        }

        public DiscoveredLocation(Level level, Vec3 vec) {
            this(level.dimension(), vec);
        }

        public TrackNodeLocation.DiscoveredLocation materialA(TrackMaterial material) {
            this.materialA = material;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation materialB(TrackMaterial material) {
            this.materialB = material;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation materials(TrackMaterial materialA, TrackMaterial materialB) {
            this.materialA = materialA;
            this.materialB = materialB;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation viaTurn(BezierConnection turn) {
            this.turn = turn;
            if (turn != null) {
                this.forceNode();
            }
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation forceNode() {
            this.forceNode = true;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation withNormal(Vec3 normal) {
            this.normal = normal;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation withYOffset(int yOffsetPixels) {
            this.yOffsetPixels = yOffsetPixels;
            return this;
        }

        public TrackNodeLocation.DiscoveredLocation withDirection(Vec3 direction) {
            this.direction = direction == null ? null : direction.normalize();
            return this;
        }

        public boolean connectedViaTurn() {
            return this.turn != null;
        }

        public BezierConnection getTurn() {
            return this.turn;
        }

        public boolean shouldForceNode() {
            return this.forceNode;
        }

        public boolean differentMaterials() {
            return this.materialA != this.materialB;
        }

        public boolean notInLineWith(Vec3 direction) {
            return this.direction != null && Math.max(direction.dot(this.direction), direction.dot(this.direction.scale(-1.0))) < 0.875;
        }

        public Vec3 getDirection() {
            return this.direction;
        }
    }
}