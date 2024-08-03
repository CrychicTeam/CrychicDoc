package com.simibubi.create.content.trains.entity;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.EdgeData;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackGraphLocation;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TravellingPoint {

    public TrackNode node1;

    public TrackNode node2;

    public TrackEdge edge;

    public double position;

    public boolean blocked;

    public boolean upsideDown;

    public TravellingPoint() {
    }

    public TravellingPoint(TrackNode node1, TrackNode node2, TrackEdge edge, double position, boolean upsideDown) {
        this.node1 = node1;
        this.node2 = node2;
        this.edge = edge;
        this.position = position;
        this.upsideDown = upsideDown;
    }

    public TravellingPoint.IEdgePointListener ignoreEdgePoints() {
        return (d, c) -> false;
    }

    public TravellingPoint.ITurnListener ignoreTurns() {
        return (d, c) -> {
        };
    }

    public TravellingPoint.IPortalListener ignorePortals() {
        return $ -> false;
    }

    public TravellingPoint.ITrackSelector random() {
        return (graph, pair) -> (Entry) ((List) pair.getSecond()).get(Create.RANDOM.nextInt(((List) pair.getSecond()).size()));
    }

    public TravellingPoint.ITrackSelector follow(TravellingPoint other) {
        return this.follow(other, null);
    }

    public TravellingPoint.ITrackSelector follow(TravellingPoint other, @Nullable Consumer<Boolean> success) {
        return (graph, pair) -> {
            List<Entry<TrackNode, TrackEdge>> validTargets = (List<Entry<TrackNode, TrackEdge>>) pair.getSecond();
            boolean forward = (Boolean) pair.getFirst();
            TrackNode target = forward ? other.node1 : other.node2;
            TrackNode secondary = forward ? other.node2 : other.node1;
            for (Entry<TrackNode, TrackEdge> entry : validTargets) {
                if (entry.getKey() == target || entry.getKey() == secondary) {
                    if (success != null) {
                        success.accept(true);
                    }
                    return entry;
                }
            }
            Vector<List<Entry<TrackNode, TrackEdge>>> frontiers = new Vector(validTargets.size());
            Vector<Set<TrackEdge>> visiteds = new Vector(validTargets.size());
            for (int j = 0; j < validTargets.size(); j++) {
                ArrayList<Entry<TrackNode, TrackEdge>> e = new ArrayList();
                Entry<TrackNode, TrackEdge> entryx = (Entry<TrackNode, TrackEdge>) validTargets.get(j);
                e.add(entryx);
                frontiers.add(e);
                HashSet<TrackEdge> e2 = new HashSet();
                e2.add((TrackEdge) entryx.getValue());
                visiteds.add(e2);
            }
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < validTargets.size(); j++) {
                    Entry<TrackNode, TrackEdge> entryx = (Entry<TrackNode, TrackEdge>) validTargets.get(j);
                    List<Entry<TrackNode, TrackEdge>> frontier = (List<Entry<TrackNode, TrackEdge>>) frontiers.get(j);
                    if (!frontier.isEmpty()) {
                        Entry<TrackNode, TrackEdge> currentEntry = (Entry<TrackNode, TrackEdge>) frontier.remove(0);
                        for (Entry<TrackNode, TrackEdge> nextEntry : graph.getConnectionsFrom((TrackNode) currentEntry.getKey()).entrySet()) {
                            TrackEdge nextEdge = (TrackEdge) nextEntry.getValue();
                            if (((Set) visiteds.get(j)).add(nextEdge) && ((TrackEdge) currentEntry.getValue()).canTravelTo(nextEdge)) {
                                TrackNode nextNode = (TrackNode) nextEntry.getKey();
                                if (nextNode == target) {
                                    if (success != null) {
                                        success.accept(true);
                                    }
                                    return entryx;
                                }
                                frontier.add(nextEntry);
                            }
                        }
                    }
                }
            }
            if (success != null) {
                success.accept(false);
            }
            return (Entry) validTargets.get(0);
        };
    }

    public TravellingPoint.ITrackSelector steer(TravellingPoint.SteerDirection direction, Vec3 upNormal) {
        return (graph, pair) -> {
            List<Entry<TrackNode, TrackEdge>> validTargets = (List<Entry<TrackNode, TrackEdge>>) pair.getSecond();
            double closest = Double.MAX_VALUE;
            Entry<TrackNode, TrackEdge> best = null;
            for (Entry<TrackNode, TrackEdge> entry : validTargets) {
                Vec3 trajectory = this.edge.getDirection(false);
                Vec3 entryTrajectory = ((TrackEdge) entry.getValue()).getDirection(true);
                Vec3 normal = trajectory.cross(upNormal);
                double dot = normal.dot(entryTrajectory);
                double diff = Math.abs((double) direction.targetDot - dot);
                if (!(diff > closest)) {
                    closest = diff;
                    best = entry;
                }
            }
            if (best == null) {
                Create.LOGGER.warn("Couldn't find steer target, choosing first");
                return (Entry) validTargets.get(0);
            } else {
                return best;
            }
        };
    }

    public double travel(TrackGraph graph, double distance, TravellingPoint.ITrackSelector trackSelector) {
        return this.travel(graph, distance, trackSelector, this.ignoreEdgePoints());
    }

    public double travel(TrackGraph graph, double distance, TravellingPoint.ITrackSelector trackSelector, TravellingPoint.IEdgePointListener signalListener) {
        return this.travel(graph, distance, trackSelector, signalListener, this.ignoreTurns());
    }

    public double travel(TrackGraph graph, double distance, TravellingPoint.ITrackSelector trackSelector, TravellingPoint.IEdgePointListener signalListener, TravellingPoint.ITurnListener turnListener) {
        return this.travel(graph, distance, trackSelector, signalListener, turnListener, this.ignorePortals());
    }

    public double travel(TrackGraph graph, double distance, TravellingPoint.ITrackSelector trackSelector, TravellingPoint.IEdgePointListener signalListener, TravellingPoint.ITurnListener turnListener, TravellingPoint.IPortalListener portalListener) {
        this.blocked = false;
        if (this.edge == null) {
            return 0.0;
        } else {
            double edgeLength = this.edge.getLength();
            if (Mth.equal(distance, 0.0)) {
                return 0.0;
            } else {
                double prevPos = this.position;
                double traveled = distance;
                double currentT = edgeLength == 0.0 ? 0.0 : this.position / edgeLength;
                double incrementT = this.edge.incrementT(currentT, distance);
                this.position = incrementT * edgeLength;
                List<Entry<TrackNode, TrackEdge>> validTargets = new ArrayList();
                boolean forward = distance > 0.0;
                double collectedDistance = forward ? -prevPos : -edgeLength + prevPos;
                Double blockedLocation = this.edgeTraversedFrom(graph, forward, signalListener, turnListener, prevPos, collectedDistance);
                if (blockedLocation != null) {
                    this.position = blockedLocation;
                    return this.position - prevPos;
                } else {
                    if (forward) {
                        while (this.position > edgeLength) {
                            validTargets.clear();
                            for (Entry<TrackNode, TrackEdge> entry : graph.getConnectionsFrom(this.node2).entrySet()) {
                                TrackNode newNode = (TrackNode) entry.getKey();
                                if (newNode != this.node1) {
                                    TrackEdge newEdge = (TrackEdge) entry.getValue();
                                    if (this.edge.canTravelTo(newEdge)) {
                                        validTargets.add(entry);
                                    }
                                }
                            }
                            if (validTargets.isEmpty()) {
                                traveled = distance - (this.position - edgeLength);
                                this.position = edgeLength;
                                this.blocked = true;
                                break;
                            }
                            Entry<TrackNode, TrackEdge> entryx = validTargets.size() == 1 ? (Entry) validTargets.get(0) : (Entry) trackSelector.apply(graph, Pair.of(true, validTargets));
                            if (((TrackEdge) entryx.getValue()).getLength() == 0.0 && portalListener.test(Couple.create(this.node2.getLocation(), ((TrackNode) entryx.getKey()).getLocation()))) {
                                traveled = distance - (this.position - edgeLength);
                                this.position = edgeLength;
                                this.blocked = true;
                                break;
                            }
                            this.node1 = this.node2;
                            this.node2 = (TrackNode) entryx.getKey();
                            this.edge = (TrackEdge) entryx.getValue();
                            this.position -= edgeLength;
                            collectedDistance += edgeLength;
                            if (this.edge.isTurn()) {
                                turnListener.accept(collectedDistance, this.edge);
                            }
                            blockedLocation = this.edgeTraversedFrom(graph, forward, signalListener, turnListener, 0.0, collectedDistance);
                            if (blockedLocation != null) {
                                traveled = distance - this.position;
                                this.position = blockedLocation;
                                traveled += this.position;
                                break;
                            }
                            prevPos = 0.0;
                            edgeLength = this.edge.getLength();
                        }
                    } else {
                        while (this.position < 0.0) {
                            validTargets.clear();
                            for (Entry<TrackNode, TrackEdge> entryxx : graph.getConnectionsFrom(this.node1).entrySet()) {
                                TrackNode newNode = (TrackNode) entryxx.getKey();
                                if (newNode != this.node2 && ((TrackEdge) graph.getConnectionsFrom(newNode).get(this.node1)).canTravelTo(this.edge)) {
                                    validTargets.add(entryxx);
                                }
                            }
                            if (validTargets.isEmpty()) {
                                traveled = distance - this.position;
                                this.position = 0.0;
                                this.blocked = true;
                                break;
                            }
                            Entry<TrackNode, TrackEdge> entryxxx = validTargets.size() == 1 ? (Entry) validTargets.get(0) : (Entry) trackSelector.apply(graph, Pair.of(false, validTargets));
                            if (((TrackEdge) entryxxx.getValue()).getLength() == 0.0 && portalListener.test(Couple.create(((TrackNode) entryxxx.getKey()).getLocation(), this.node1.getLocation()))) {
                                traveled = distance - this.position;
                                this.position = 0.0;
                                this.blocked = true;
                                break;
                            }
                            this.node2 = this.node1;
                            this.node1 = (TrackNode) entryxxx.getKey();
                            this.edge = (TrackEdge) graph.getConnectionsFrom(this.node1).get(this.node2);
                            collectedDistance += edgeLength;
                            edgeLength = this.edge.getLength();
                            this.position += edgeLength;
                            blockedLocation = this.edgeTraversedFrom(graph, forward, signalListener, turnListener, edgeLength, collectedDistance);
                            if (blockedLocation != null) {
                                traveled = distance - this.position;
                                this.position = blockedLocation;
                                traveled += this.position;
                                break;
                            }
                        }
                    }
                    return traveled;
                }
            }
        }
    }

    protected Double edgeTraversedFrom(TrackGraph graph, boolean forward, TravellingPoint.IEdgePointListener edgePointListener, TravellingPoint.ITurnListener turnListener, double prevPos, double totalDistance) {
        if (this.edge.isTurn()) {
            turnListener.accept(Math.max(0.0, totalDistance), this.edge);
        }
        double from = forward ? prevPos : this.position;
        double to = forward ? this.position : prevPos;
        EdgeData edgeData = this.edge.getEdgeData();
        List<TrackEdgePoint> edgePoints = edgeData.getPoints();
        double length = this.edge.getLength();
        for (int i = 0; i < edgePoints.size(); i++) {
            int index = forward ? i : edgePoints.size() - i - 1;
            TrackEdgePoint nextBoundary = (TrackEdgePoint) edgePoints.get(index);
            double locationOn = nextBoundary.getLocationOn(this.edge);
            double distance = forward ? locationOn : length - locationOn;
            if (forward ? !(locationOn < from) && !(locationOn >= to) : !(locationOn <= from) && !(locationOn > to)) {
                Couple<TrackNode> nodes = Couple.create(this.node1, this.node2);
                if (edgePointListener.test(totalDistance + distance, Pair.of(nextBoundary, forward ? nodes : nodes.swap()))) {
                    return locationOn;
                }
            }
        }
        return null;
    }

    public void reverse(TrackGraph graph) {
        TrackNode n = this.node1;
        this.node1 = this.node2;
        this.node2 = n;
        this.position = this.edge.getLength() - this.position;
        this.edge = (TrackEdge) graph.getConnectionsFrom(this.node1).get(this.node2);
    }

    public Vec3 getPosition(@Nullable TrackGraph trackGraph) {
        return this.getPosition(trackGraph, false);
    }

    public Vec3 getPosition(@Nullable TrackGraph trackGraph, boolean flipUpsideDown) {
        return this.getPositionWithOffset(trackGraph, 0.0, flipUpsideDown);
    }

    public Vec3 getPositionWithOffset(@Nullable TrackGraph trackGraph, double offset, boolean flipUpsideDown) {
        double t = (this.position + offset) / this.edge.getLength();
        return this.edge.getPosition(trackGraph, t).add(this.edge.getNormal(trackGraph, t).scale(this.upsideDown ^ flipUpsideDown ? -1.0 : 1.0));
    }

    public void migrateTo(List<TrackGraphLocation> locations) {
        TrackGraphLocation location = (TrackGraphLocation) locations.remove(0);
        TrackGraph graph = location.graph;
        this.node1 = graph.locateNode(location.edge.getFirst());
        this.node2 = graph.locateNode(location.edge.getSecond());
        this.position = location.position;
        this.edge = (TrackEdge) graph.getConnectionsFrom(this.node1).get(this.node2);
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        Couple<TrackNode> nodes = Couple.create(this.node1, this.node2);
        if (nodes.either(Objects::isNull)) {
            return tag;
        } else {
            tag.put("Nodes", nodes.map(TrackNode::getLocation).serializeEach(loc -> loc.write(dimensions)));
            tag.putDouble("Position", this.position);
            tag.putBoolean("UpsideDown", this.upsideDown);
            return tag;
        }
    }

    public static TravellingPoint read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions) {
        if (graph == null) {
            return new TravellingPoint(null, null, null, 0.0, false);
        } else {
            Couple<TrackNode> locs = tag.contains("Nodes") ? Couple.deserializeEach(tag.getList("Nodes", 10), c -> TrackNodeLocation.read(c, dimensions)).map(graph::locateNode) : Couple.create(null, null);
            if (locs.either(Objects::isNull)) {
                return new TravellingPoint(null, null, null, 0.0, false);
            } else {
                double position = tag.getDouble("Position");
                return new TravellingPoint(locs.getFirst(), locs.getSecond(), (TrackEdge) graph.getConnectionsFrom(locs.getFirst()).get(locs.getSecond()), position, tag.getBoolean("UpsideDown"));
            }
        }
    }

    public interface IEdgePointListener extends BiPredicate<Double, Pair<TrackEdgePoint, Couple<TrackNode>>> {
    }

    public interface IPortalListener extends Predicate<Couple<TrackNodeLocation>> {
    }

    public interface ITrackSelector extends BiFunction<TrackGraph, Pair<Boolean, List<Entry<TrackNode, TrackEdge>>>, Entry<TrackNode, TrackEdge>> {
    }

    public interface ITurnListener extends BiConsumer<Double, TrackEdge> {
    }

    public static enum SteerDirection {

        NONE(0.0F), LEFT(-1.0F), RIGHT(1.0F);

        float targetDot;

        private SteerDirection(float targetDot) {
            this.targetDot = targetDot;
        }
    }
}