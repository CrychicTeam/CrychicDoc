package com.simibubi.create.content.trains.graph;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TrackEdge {

    public TrackNode node1;

    public TrackNode node2;

    BezierConnection turn;

    EdgeData edgeData;

    boolean interDimensional;

    TrackMaterial trackMaterial;

    public TrackEdge(TrackNode node1, TrackNode node2, BezierConnection turn, TrackMaterial trackMaterial) {
        this.interDimensional = !node1.location.dimension.equals(node2.location.dimension);
        this.edgeData = new EdgeData(this);
        this.node1 = node1;
        this.node2 = node2;
        this.turn = turn;
        this.trackMaterial = trackMaterial;
    }

    public TrackMaterial getTrackMaterial() {
        return this.trackMaterial;
    }

    public boolean isTurn() {
        return this.turn != null;
    }

    public boolean isInterDimensional() {
        return this.interDimensional;
    }

    public EdgeData getEdgeData() {
        return this.edgeData;
    }

    public BezierConnection getTurn() {
        return this.turn;
    }

    public Vec3 getDirection(boolean fromFirst) {
        return this.getPosition(null, fromFirst ? 0.25 : 1.0).subtract(this.getPosition(null, fromFirst ? 0.0 : 0.75)).normalize();
    }

    public Vec3 getDirectionAt(double t) {
        double length = this.getLength();
        double step = 0.5 / length;
        t /= length;
        Vec3 ahead = this.getPosition(null, Math.min(1.0, t + step));
        Vec3 behind = this.getPosition(null, Math.max(0.0, t - step));
        return ahead.subtract(behind).normalize();
    }

    public boolean canTravelTo(TrackEdge other) {
        if (!this.isInterDimensional() && !other.isInterDimensional()) {
            Vec3 newDirection = other.getDirection(true);
            return this.getDirection(false).dot(newDirection) > 0.875;
        } else {
            return true;
        }
    }

    public double getLength() {
        return this.isInterDimensional() ? 0.0 : (this.isTurn() ? this.turn.getLength() : this.node1.location.getLocation().distanceTo(this.node2.location.getLocation()));
    }

    public double incrementT(double currentT, double distance) {
        boolean tooFar = Math.abs(distance) > 5.0;
        double length = this.getLength();
        distance /= length == 0.0 ? 1.0 : length;
        return !tooFar && this.isTurn() ? this.turn.incrementT(currentT, distance) : currentT + distance;
    }

    public Vec3 getPosition(@Nullable TrackGraph trackGraph, double t) {
        if (this.isTurn()) {
            return this.turn.getPosition(Mth.clamp(t, 0.0, 1.0));
        } else {
            if (trackGraph != null && (this.node1.location.yOffsetPixels != 0 || this.node2.location.yOffsetPixels != 0)) {
                Vec3 positionSmoothed = this.getPositionSmoothed(trackGraph, t);
                if (positionSmoothed != null) {
                    return positionSmoothed;
                }
            }
            return VecHelper.lerp((float) t, this.node1.location.getLocation(), this.node2.location.getLocation());
        }
    }

    public Vec3 getNormal(@Nullable TrackGraph trackGraph, double t) {
        if (this.isTurn()) {
            return this.turn.getNormal(Mth.clamp(t, 0.0, 1.0));
        } else {
            if (trackGraph != null && (this.node1.location.yOffsetPixels != 0 || this.node2.location.yOffsetPixels != 0)) {
                Vec3 normalSmoothed = this.getNormalSmoothed(trackGraph, t);
                if (normalSmoothed != null) {
                    return normalSmoothed;
                }
            }
            return this.node1.getNormal();
        }
    }

    @Nullable
    public Vec3 getPositionSmoothed(TrackGraph trackGraph, double t) {
        Vec3 node1Location = null;
        Vec3 node2Location = null;
        for (TrackEdge trackEdge : trackGraph.getConnectionsFrom(this.node1).values()) {
            if (trackEdge.isTurn()) {
                node1Location = trackEdge.getPosition(trackGraph, 0.0);
            }
        }
        for (TrackEdge trackEdgex : trackGraph.getConnectionsFrom(this.node2).values()) {
            if (trackEdgex.isTurn()) {
                node2Location = trackEdgex.getPosition(trackGraph, 0.0);
            }
        }
        return node1Location != null && node2Location != null ? VecHelper.lerp((float) t, node1Location, node2Location) : null;
    }

    @Nullable
    public Vec3 getNormalSmoothed(TrackGraph trackGraph, double t) {
        Vec3 node1Normal = null;
        Vec3 node2Normal = null;
        for (TrackEdge trackEdge : trackGraph.getConnectionsFrom(this.node1).values()) {
            if (trackEdge.isTurn()) {
                node1Normal = trackEdge.getNormal(trackGraph, 0.0);
            }
        }
        for (TrackEdge trackEdgex : trackGraph.getConnectionsFrom(this.node2).values()) {
            if (trackEdgex.isTurn()) {
                node2Normal = trackEdgex.getNormal(trackGraph, 0.0);
            }
        }
        return node1Normal != null && node2Normal != null ? VecHelper.lerp(0.5F, node1Normal, node2Normal) : null;
    }

    public Collection<double[]> getIntersection(TrackNode node1, TrackNode node2, TrackEdge other, TrackNode other1, TrackNode other2) {
        Vec3 v1 = node1.location.getLocation();
        Vec3 v2 = node2.location.getLocation();
        Vec3 w1 = other1.location.getLocation();
        Vec3 w2 = other2.location.getLocation();
        if (!this.isInterDimensional() && !other.isInterDimensional()) {
            if (v1.y == v2.y && v1.y == w1.y && v1.y == w2.y) {
                if (!this.isTurn()) {
                    return (Collection<double[]>) (!other.isTurn() ? ImmutableList.of(VecHelper.intersectRanged(v1, w1, v2, w2, Direction.Axis.Y)) : other.getIntersection(other1, other2, this, node1, node2).stream().map(a -> new double[] { a[1], a[0] }).toList());
                } else {
                    AABB bb = this.turn.getBounds();
                    if (!other.isTurn()) {
                        if (!bb.intersects(w1, w2)) {
                            return Collections.emptyList();
                        } else {
                            Vec3 seg1 = v1;
                            Vec3 seg2 = null;
                            double t = 0.0;
                            Collection<double[]> intersections = new ArrayList();
                            for (int i = 0; i < this.turn.getSegmentCount(); i++) {
                                double tOffset = t;
                                t += 0.5;
                                seg2 = this.getPosition(null, t / this.getLength());
                                double[] intersection = VecHelper.intersectRanged(seg1, w1, seg2, w2, Direction.Axis.Y);
                                seg1 = seg2;
                                if (intersection != null) {
                                    intersection[0] += tOffset;
                                    intersections.add(intersection);
                                }
                            }
                            return intersections;
                        }
                    } else if (!bb.intersects(other.turn.getBounds())) {
                        return Collections.emptyList();
                    } else {
                        Vec3 seg1 = v1;
                        Vec3 seg2 = null;
                        double t = 0.0;
                        Collection<double[]> intersections = new ArrayList();
                        for (int ix = 0; ix < this.turn.getSegmentCount(); ix++) {
                            double tOffset = t;
                            t += 0.5;
                            seg2 = this.getPosition(null, t / this.getLength());
                            Vec3 otherSeg1 = w1;
                            Vec3 otherSeg2 = null;
                            double u = 0.0;
                            for (int j = 0; j < other.turn.getSegmentCount(); j++) {
                                double uOffset = u;
                                u += 0.5;
                                otherSeg2 = other.getPosition(null, u / other.getLength());
                                double[] intersection = VecHelper.intersectRanged(seg1, otherSeg1, seg2, otherSeg2, Direction.Axis.Y);
                                otherSeg1 = otherSeg2;
                                if (intersection != null) {
                                    intersection[0] += tOffset;
                                    intersection[1] += uOffset;
                                    intersections.add(intersection);
                                }
                            }
                            seg1 = seg2;
                        }
                        return intersections;
                    }
                }
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag baseCompound = this.isTurn() ? this.turn.write(BlockPos.ZERO) : new CompoundTag();
        baseCompound.put("Signals", this.edgeData.write(dimensions));
        baseCompound.putString("Material", this.getTrackMaterial().id.toString());
        return baseCompound;
    }

    public static TrackEdge read(TrackNode node1, TrackNode node2, CompoundTag tag, TrackGraph graph, DimensionPalette dimensions) {
        TrackEdge trackEdge = new TrackEdge(node1, node2, tag.contains("Positions") ? new BezierConnection(tag, BlockPos.ZERO) : null, TrackMaterial.deserialize(tag.getString("Material")));
        trackEdge.edgeData = EdgeData.read(tag.getCompound("Signals"), trackEdge, graph, dimensions);
        return trackEdge;
    }
}