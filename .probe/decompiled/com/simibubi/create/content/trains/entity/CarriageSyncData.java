package com.simibubi.create.content.trains.entity;

import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class CarriageSyncData {

    public Vector<Pair<Couple<Integer>, Float>> wheelLocations = new Vector(4);

    public Pair<Vec3, Couple<Vec3>> fallbackLocations = null;

    public float distanceToDestination;

    public boolean leadingCarriage;

    private Pair<Vec3, Couple<Vec3>> fallbackPointSnapshot;

    private TravellingPoint[] pointsToApproach;

    private float[] pointDistanceSnapshot = new float[4];

    private float destinationDistanceSnapshot;

    private int ticksSince;

    private boolean isDirty;

    public CarriageSyncData() {
        this.pointsToApproach = new TravellingPoint[4];
        this.fallbackPointSnapshot = null;
        this.destinationDistanceSnapshot = 0.0F;
        this.leadingCarriage = false;
        this.ticksSince = 0;
        for (int i = 0; i < 4; i++) {
            this.wheelLocations.add(null);
            this.pointsToApproach[i] = new TravellingPoint();
        }
    }

    public CarriageSyncData copy() {
        CarriageSyncData data = new CarriageSyncData();
        for (int i = 0; i < 4; i++) {
            data.wheelLocations.set(i, (Pair) this.wheelLocations.get(i));
        }
        if (this.fallbackLocations != null) {
            data.fallbackLocations = this.fallbackLocations.copy();
        }
        data.distanceToDestination = this.distanceToDestination;
        data.leadingCarriage = this.leadingCarriage;
        return data;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.leadingCarriage);
        buffer.writeBoolean(this.fallbackLocations != null);
        if (this.fallbackLocations != null) {
            Vec3 contraptionAnchor = this.fallbackLocations.getFirst();
            Couple<Vec3> rotationAnchors = this.fallbackLocations.getSecond();
            VecHelper.write(contraptionAnchor, buffer);
            VecHelper.write(rotationAnchors.getFirst(), buffer);
            VecHelper.write(rotationAnchors.getSecond(), buffer);
        } else {
            for (Pair<Couple<Integer>, Float> pair : this.wheelLocations) {
                buffer.writeBoolean(pair == null);
                if (pair == null) {
                    break;
                }
                pair.getFirst().forEach(buffer::writeInt);
                buffer.writeFloat(pair.getSecond());
            }
            buffer.writeFloat(this.distanceToDestination);
        }
    }

    public void read(FriendlyByteBuf buffer) {
        this.leadingCarriage = buffer.readBoolean();
        boolean fallback = buffer.readBoolean();
        this.ticksSince = 0;
        if (fallback) {
            this.fallbackLocations = Pair.of(VecHelper.read(buffer), Couple.create(VecHelper.read(buffer), VecHelper.read(buffer)));
        } else {
            this.fallbackLocations = null;
            for (int i = 0; i < 4 && !buffer.readBoolean(); i++) {
                this.wheelLocations.set(i, Pair.of(Couple.create(buffer::readInt), buffer.readFloat()));
            }
            this.distanceToDestination = buffer.readFloat();
        }
    }

    public void update(CarriageContraptionEntity entity, Carriage carriage) {
        Carriage.DimensionalCarriageEntity dce = carriage.getDimensional(entity.m_9236_());
        TrackGraph graph = carriage.train.graph;
        if (graph == null) {
            this.updateFallbackLocations(dce);
        } else {
            this.fallbackLocations = null;
            this.leadingCarriage = entity.carriageIndex == (carriage.train.speed >= 0.0 ? 0 : carriage.train.carriages.size() - 1);
            for (boolean first : Iterate.trueAndFalse) {
                if (!first && !carriage.isOnTwoBogeys()) {
                    break;
                }
                CarriageBogey bogey = carriage.bogeys.get(first);
                for (boolean firstPoint : Iterate.trueAndFalse) {
                    TravellingPoint point = bogey.points.get(firstPoint);
                    int index = (first ? 0 : 2) + (firstPoint ? 0 : 1);
                    Couple<TrackNode> nodes = Couple.create(point.node1, point.node2);
                    if (nodes.either(Objects::isNull)) {
                        this.updateFallbackLocations(dce);
                        return;
                    }
                    this.wheelLocations.set(index, Pair.of(nodes.map(TrackNode::getNetId), (float) point.position));
                }
            }
            this.distanceToDestination = (float) carriage.train.navigation.distanceToDestination;
            this.setDirty(true);
        }
    }

    private void updateFallbackLocations(Carriage.DimensionalCarriageEntity dce) {
        this.fallbackLocations = Pair.of(dce.positionAnchor, dce.rotationAnchors);
        dce.pointsInitialised = true;
        this.setDirty(true);
    }

    public void apply(CarriageContraptionEntity entity, Carriage carriage) {
        Carriage.DimensionalCarriageEntity dce = carriage.getDimensional(entity.m_9236_());
        this.fallbackPointSnapshot = null;
        if (this.fallbackLocations != null) {
            this.fallbackPointSnapshot = Pair.of(dce.positionAnchor, dce.rotationAnchors);
            dce.pointsInitialised = true;
        } else {
            TrackGraph graph = carriage.train.graph;
            if (graph != null) {
                for (int i = 0; i < this.wheelLocations.size(); i++) {
                    Pair<Couple<Integer>, Float> pair = (Pair<Couple<Integer>, Float>) this.wheelLocations.get(i);
                    if (pair == null) {
                        break;
                    }
                    CarriageBogey bogey = carriage.bogeys.get(i / 2 == 0);
                    TravellingPoint bogeyPoint = bogey.points.get(i % 2 == 0);
                    TravellingPoint point = dce.pointsInitialised ? this.pointsToApproach[i] : bogeyPoint;
                    Couple<TrackNode> nodes = pair.getFirst().map(graph::getNode);
                    if (!nodes.either(Objects::isNull)) {
                        TrackEdge edge = (TrackEdge) graph.getConnectionsFrom(nodes.getFirst()).get(nodes.getSecond());
                        if (edge != null) {
                            point.node1 = nodes.getFirst();
                            point.node2 = nodes.getSecond();
                            point.edge = edge;
                            point.position = (double) pair.getSecond().floatValue();
                            if (dce.pointsInitialised) {
                                float foundDistance = -1.0F;
                                boolean direction = false;
                                for (boolean forward : Iterate.trueAndFalse) {
                                    float distanceTo = this.getDistanceTo(graph, bogeyPoint, point, foundDistance, forward);
                                    if (distanceTo > 0.0F && (foundDistance == -1.0F || distanceTo < foundDistance)) {
                                        foundDistance = distanceTo;
                                        direction = forward;
                                    }
                                }
                                if (foundDistance != -1.0F) {
                                    this.pointDistanceSnapshot[i] = (float) (direction ? 1 : -1) * foundDistance;
                                } else {
                                    bogeyPoint.node1 = point.node1;
                                    bogeyPoint.node2 = point.node2;
                                    bogeyPoint.edge = point.edge;
                                    bogeyPoint.position = point.position;
                                    this.pointDistanceSnapshot[i] = 0.0F;
                                }
                            }
                        }
                    }
                }
                if (!dce.pointsInitialised) {
                    carriage.train.navigation.distanceToDestination = (double) this.distanceToDestination;
                    dce.pointsInitialised = true;
                } else if (this.leadingCarriage) {
                    this.destinationDistanceSnapshot = (float) ((double) this.distanceToDestination - carriage.train.navigation.distanceToDestination);
                }
            }
        }
    }

    public void approach(CarriageContraptionEntity entity, Carriage carriage, float partialIn) {
        Carriage.DimensionalCarriageEntity dce = carriage.getDimensional(entity.m_9236_());
        int updateInterval = entity.m_6095_().updateInterval();
        if (this.ticksSince >= updateInterval * 2) {
            partialIn /= (float) (this.ticksSince - updateInterval * 2 + 1);
        }
        partialIn *= ServerSpeedProvider.get();
        float partial = partialIn;
        this.ticksSince++;
        if (this.fallbackLocations != null && this.fallbackPointSnapshot != null) {
            dce.positionAnchor = this.approachVector(partialIn, dce.positionAnchor, this.fallbackLocations.getFirst(), this.fallbackPointSnapshot.getFirst());
            dce.rotationAnchors.replaceWithContext((current, firstx) -> this.approachVector(partialIn, current, this.fallbackLocations.getSecond().get(firstx), this.fallbackPointSnapshot.getSecond().get(firstx)));
        } else {
            TrackGraph graph = carriage.train.graph;
            if (graph != null) {
                carriage.train.navigation.distanceToDestination = carriage.train.navigation.distanceToDestination + (double) (partialIn * this.destinationDistanceSnapshot);
                for (boolean first : Iterate.trueAndFalse) {
                    if (!first && !carriage.isOnTwoBogeys()) {
                        break;
                    }
                    CarriageBogey bogey = carriage.bogeys.get(first);
                    for (boolean firstPoint : Iterate.trueAndFalse) {
                        int index = (first ? 0 : 2) + (firstPoint ? 0 : 1);
                        float f = this.pointDistanceSnapshot[index];
                        if (!Mth.equal(f, 0.0F)) {
                            TravellingPoint point = bogey.points.get(firstPoint);
                            MutableBoolean success = new MutableBoolean(true);
                            TravellingPoint toApproach = this.pointsToApproach[index];
                            TravellingPoint.ITrackSelector trackSelector = point.follow(toApproach, b -> success.setValue(success.booleanValue() && b));
                            point.travel(graph, (double) (partial * f), trackSelector);
                            if (!success.booleanValue()) {
                                point.node1 = toApproach.node1;
                                point.node2 = toApproach.node2;
                                point.edge = toApproach.edge;
                                point.position = toApproach.position;
                                this.pointDistanceSnapshot[index] = 0.0F;
                            }
                        }
                    }
                }
            }
        }
    }

    private Vec3 approachVector(float partial, Vec3 current, Vec3 target, Vec3 snapshot) {
        return current != null && snapshot != null ? current.add(target.subtract(snapshot).scale((double) partial)) : target;
    }

    public float getDistanceTo(TrackGraph graph, TravellingPoint current, TravellingPoint target, float maxDistance, boolean forward) {
        if (maxDistance == -1.0F) {
            maxDistance = 32.0F;
        }
        Set<TrackEdge> visited = new HashSet();
        Map<TrackEdge, Pair<Boolean, TrackEdge>> reachedVia = new IdentityHashMap();
        PriorityQueue<Pair<Double, Pair<Couple<TrackNode>, TrackEdge>>> frontier = new PriorityQueue((p1, p2) -> Double.compare((Double) p1.getFirst(), (Double) p2.getFirst()));
        TrackNode initialNode1 = forward ? current.node1 : current.node2;
        TrackNode initialNode2 = forward ? current.node2 : current.node1;
        Map<TrackNode, TrackEdge> connectionsFromInitial = graph.getConnectionsFrom(initialNode1);
        if (connectionsFromInitial == null) {
            return -1.0F;
        } else {
            TrackEdge initialEdge = (TrackEdge) connectionsFromInitial.get(initialNode2);
            if (initialEdge == null) {
                return -1.0F;
            } else {
                TrackNode targetNode1 = forward ? target.node1 : target.node2;
                TrackNode targetNode2 = forward ? target.node2 : target.node1;
                TrackEdge targetEdge = (TrackEdge) graph.getConnectionsFrom(targetNode1).get(targetNode2);
                double distanceToNode2 = forward ? initialEdge.getLength() - current.position : current.position;
                frontier.add(Pair.of(distanceToNode2, Pair.of(Couple.create(initialNode1, initialNode2), initialEdge)));
                while (!frontier.isEmpty()) {
                    Pair<Double, Pair<Couple<TrackNode>, TrackEdge>> poll = (Pair<Double, Pair<Couple<TrackNode>, TrackEdge>>) frontier.poll();
                    double distance = poll.getFirst();
                    Pair<Couple<TrackNode>, TrackEdge> currentEntry = poll.getSecond();
                    TrackNode node2 = currentEntry.getFirst().getSecond();
                    TrackEdge edge = currentEntry.getSecond();
                    if (edge == targetEdge) {
                        return (float) (distance - (forward ? edge.getLength() - target.position : target.position));
                    }
                    if (!(distance > (double) maxDistance)) {
                        List<Entry<TrackNode, TrackEdge>> validTargets = new ArrayList();
                        Map<TrackNode, TrackEdge> connectionsFrom = graph.getConnectionsFrom(node2);
                        for (Entry<TrackNode, TrackEdge> entry : connectionsFrom.entrySet()) {
                            TrackEdge newEdge = (TrackEdge) entry.getValue();
                            Vec3 currentDirection = edge.getDirection(false);
                            Vec3 newDirection = newEdge.getDirection(true);
                            if (!(currentDirection.dot(newDirection) < 0.875) && visited.add((TrackEdge) entry.getValue())) {
                                validTargets.add(entry);
                            }
                        }
                        if (!validTargets.isEmpty()) {
                            for (Entry<TrackNode, TrackEdge> entryx : validTargets) {
                                TrackNode newNode = (TrackNode) entryx.getKey();
                                TrackEdge newEdge = (TrackEdge) entryx.getValue();
                                reachedVia.put(newEdge, Pair.of(validTargets.size() > 1, edge));
                                frontier.add(Pair.of(newEdge.getLength() + distance, Pair.of(Couple.create(node2, newNode), newEdge)));
                            }
                        }
                    }
                }
                return -1.0F;
            }
        }
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}