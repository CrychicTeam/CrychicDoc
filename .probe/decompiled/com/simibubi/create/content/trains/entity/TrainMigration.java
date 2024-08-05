package com.simibubi.create.content.trains.entity;

import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackGraphLocation;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TrainMigration {

    Couple<TrackNodeLocation> locations;

    double positionOnOldEdge;

    boolean curve;

    Vec3 fallback;

    public TrainMigration() {
    }

    public TrainMigration(TravellingPoint point) {
        double t = point.position / point.edge.getLength();
        this.fallback = point.edge.getPosition(null, t);
        this.curve = point.edge.isTurn();
        this.positionOnOldEdge = point.position;
        this.locations = Couple.create(point.node1.getLocation(), point.node2.getLocation());
    }

    public TrackGraphLocation tryMigratingTo(TrackGraph graph) {
        TrackNode node1 = graph.locateNode(this.locations.getFirst());
        TrackNode node2 = graph.locateNode(this.locations.getSecond());
        if (node1 != null && node2 != null) {
            TrackEdge edge = (TrackEdge) graph.getConnectionsFrom(node1).get(node2);
            if (edge != null) {
                TrackGraphLocation graphLocation = new TrackGraphLocation();
                graphLocation.graph = graph;
                graphLocation.edge = this.locations;
                graphLocation.position = this.positionOnOldEdge;
                return graphLocation;
            }
        }
        if (this.curve) {
            return null;
        } else {
            Vec3 prevDirection = this.locations.getSecond().getLocation().subtract(this.locations.getFirst().getLocation()).normalize();
            for (TrackNodeLocation loc : graph.getNodes()) {
                Vec3 nodeVec = loc.getLocation();
                if (!(nodeVec.distanceToSqr(this.fallback) > 1024.0)) {
                    TrackNode newNode1 = graph.locateNode(loc);
                    for (Entry<TrackNode, TrackEdge> entry : graph.getConnectionsFrom(newNode1).entrySet()) {
                        TrackEdge edge = (TrackEdge) entry.getValue();
                        if (!edge.isTurn()) {
                            TrackNode newNode2 = (TrackNode) entry.getKey();
                            float radius = 0.015625F;
                            Vec3 direction = edge.getDirection(true);
                            if (Mth.equal(direction.dot(prevDirection), 1.0)) {
                                Vec3 intersectSphere = VecHelper.intersectSphere(nodeVec, direction, this.fallback, (double) radius);
                                if (intersectSphere != null && Mth.equal(direction.dot(intersectSphere.subtract(nodeVec).normalize()), 1.0)) {
                                    double edgeLength = edge.getLength();
                                    double position = intersectSphere.distanceTo(nodeVec) - (double) radius;
                                    if (!Double.isNaN(position) && !(position < 0.0) && !(position > edgeLength)) {
                                        TrackGraphLocation graphLocation = new TrackGraphLocation();
                                        graphLocation.graph = graph;
                                        graphLocation.edge = Couple.create(loc, newNode2.getLocation());
                                        graphLocation.position = position;
                                        return graphLocation;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Curve", this.curve);
        tag.put("Fallback", VecHelper.writeNBT(this.fallback));
        tag.putDouble("Position", this.positionOnOldEdge);
        tag.put("Nodes", this.locations.serializeEach(l -> l.write(dimensions)));
        return tag;
    }

    public static TrainMigration read(CompoundTag tag, DimensionPalette dimensions) {
        TrainMigration trainMigration = new TrainMigration();
        trainMigration.curve = tag.getBoolean("Curve");
        trainMigration.fallback = VecHelper.readNBT(tag.getList("Fallback", 6));
        trainMigration.positionOnOldEdge = tag.getDouble("Position");
        trainMigration.locations = Couple.deserializeEach(tag.getList("Nodes", 10), c -> TrackNodeLocation.read(c, dimensions));
        return trainMigration;
    }
}