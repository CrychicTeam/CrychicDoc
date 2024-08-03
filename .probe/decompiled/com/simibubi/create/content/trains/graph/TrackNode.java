package com.simibubi.create.content.trains.graph;

import net.minecraft.world.phys.Vec3;

public class TrackNode {

    int netId;

    Vec3 normal;

    TrackNodeLocation location;

    public TrackNode(TrackNodeLocation location, int netId, Vec3 normal) {
        this.location = location;
        this.netId = netId;
        this.normal = normal;
    }

    public TrackNodeLocation getLocation() {
        return this.location;
    }

    public int getNetId() {
        return this.netId;
    }

    public Vec3 getNormal() {
        return this.normal;
    }
}