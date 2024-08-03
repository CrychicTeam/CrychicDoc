package com.simibubi.create.content.trains.graph;

import com.simibubi.create.foundation.utility.Couple;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

public class TrackEdgeIntersection {

    public double location;

    public Couple<TrackNodeLocation> target;

    public double targetLocation;

    public UUID groupId;

    public UUID id = UUID.randomUUID();

    public boolean isNear(double location) {
        return Math.abs(location - this.location) < 0.03125;
    }

    public boolean targets(TrackNodeLocation target1, TrackNodeLocation target2) {
        return target1.equals(this.target.getFirst()) && target2.equals(this.target.getSecond()) || target1.equals(this.target.getSecond()) && target2.equals(this.target.getFirst());
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("Id", this.id);
        if (this.groupId != null) {
            nbt.putUUID("GroupId", this.groupId);
        }
        nbt.putDouble("Location", this.location);
        nbt.putDouble("TargetLocation", this.targetLocation);
        nbt.put("TargetEdge", this.target.serializeEach(loc -> loc.write(dimensions)));
        return nbt;
    }

    public static TrackEdgeIntersection read(CompoundTag nbt, DimensionPalette dimensions) {
        TrackEdgeIntersection intersection = new TrackEdgeIntersection();
        intersection.id = nbt.getUUID("Id");
        if (nbt.contains("GroupId")) {
            intersection.groupId = nbt.getUUID("GroupId");
        }
        intersection.location = nbt.getDouble("Location");
        intersection.targetLocation = nbt.getDouble("TargetLocation");
        intersection.target = Couple.deserializeEach(nbt.getList("TargetEdge", 10), tag -> TrackNodeLocation.read(tag, dimensions));
        return intersection;
    }
}