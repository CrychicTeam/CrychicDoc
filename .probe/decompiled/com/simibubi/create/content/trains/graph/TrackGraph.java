package com.simibubi.create.content.trains.graph;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class TrackGraph {

    public static final AtomicInteger graphNetIdGenerator = new AtomicInteger();

    public static final AtomicInteger nodeNetIdGenerator = new AtomicInteger();

    public UUID id;

    public Color color;

    Map<TrackNodeLocation, TrackNode> nodes;

    Map<Integer, TrackNode> nodesById;

    Map<TrackNode, Map<TrackNode, TrackEdge>> connectionsByNode;

    EdgePointStorage edgePoints;

    Map<ResourceKey<Level>, TrackGraphBounds> bounds;

    List<TrackEdge> deferredIntersectionUpdates;

    int netId;

    int checksum = 0;

    public TrackGraph() {
        this(UUID.randomUUID());
    }

    public TrackGraph(UUID graphID) {
        this.setId(graphID);
        this.nodes = new HashMap();
        this.nodesById = new HashMap();
        this.bounds = new HashMap();
        this.connectionsByNode = new IdentityHashMap();
        this.edgePoints = new EdgePointStorage();
        this.deferredIntersectionUpdates = new ArrayList();
        this.netId = nextGraphId();
    }

    public <T extends TrackEdgePoint> void addPoint(EdgePointType<T> type, T point) {
        this.edgePoints.put(type, point);
        EdgePointManager.onEdgePointAdded(this, point, type);
        Create.RAILWAYS.sync.pointAdded(this, point);
        this.markDirty();
    }

    public <T extends TrackEdgePoint> T getPoint(EdgePointType<T> type, UUID id) {
        return this.edgePoints.get(type, id);
    }

    public <T extends TrackEdgePoint> Collection<T> getPoints(EdgePointType<T> type) {
        return this.edgePoints.values(type);
    }

    public <T extends TrackEdgePoint> T removePoint(EdgePointType<T> type, UUID id) {
        T removed = this.edgePoints.remove(type, id);
        if (removed == null) {
            return null;
        } else {
            EdgePointManager.onEdgePointRemoved(this, removed, type);
            Create.RAILWAYS.sync.pointRemoved(this, removed);
            this.markDirty();
            return removed;
        }
    }

    public void tickPoints(boolean preTrains) {
        this.edgePoints.tick(this, preTrains);
    }

    public TrackGraphBounds getBounds(Level level) {
        return (TrackGraphBounds) this.bounds.computeIfAbsent(level.dimension(), dim -> new TrackGraphBounds(this, dim));
    }

    public void invalidateBounds() {
        this.checksum = 0;
        this.bounds.clear();
    }

    public Set<TrackNodeLocation> getNodes() {
        return this.nodes.keySet();
    }

    public TrackNode locateNode(Level level, Vec3 position) {
        return this.locateNode(new TrackNodeLocation(position).in(level));
    }

    public TrackNode locateNode(TrackNodeLocation position) {
        return (TrackNode) this.nodes.get(position);
    }

    public TrackNode getNode(int netId) {
        return (TrackNode) this.nodesById.get(netId);
    }

    public boolean createNodeIfAbsent(TrackNodeLocation.DiscoveredLocation location) {
        if (!this.addNodeIfAbsent(new TrackNode(location, nextNodeId(), location.normal))) {
            return false;
        } else {
            TrackNode newNode = (TrackNode) this.nodes.get(location);
            Create.RAILWAYS.sync.nodeAdded(this, newNode);
            this.invalidateBounds();
            this.markDirty();
            return true;
        }
    }

    public void loadNode(TrackNodeLocation location, int netId, Vec3 normal) {
        this.addNode(new TrackNode(location, netId, normal));
    }

    public void addNode(TrackNode node) {
        TrackNodeLocation location = node.getLocation();
        if (this.nodes.containsKey(location)) {
            this.removeNode(null, location);
        }
        this.nodes.put(location, node);
        this.nodesById.put(node.getNetId(), node);
    }

    public boolean addNodeIfAbsent(TrackNode node) {
        if (this.nodes.putIfAbsent(node.getLocation(), node) != null) {
            return false;
        } else {
            this.nodesById.put(node.getNetId(), node);
            return true;
        }
    }

    public boolean removeNode(@Nullable LevelAccessor level, TrackNodeLocation location) {
        TrackNode removed = (TrackNode) this.nodes.remove(location);
        if (removed == null) {
            return false;
        } else {
            Map<UUID, Train> trains = Create.RAILWAYS.trains;
            for (UUID uuid : trains.keySet()) {
                Train train = (Train) trains.get(uuid);
                if (train.graph == this && train.isTravellingOn(removed)) {
                    train.detachFromTracks();
                }
            }
            this.nodesById.remove(removed.netId);
            this.invalidateBounds();
            if (!this.connectionsByNode.containsKey(removed)) {
                return true;
            } else {
                Map<TrackNode, TrackEdge> connections = (Map<TrackNode, TrackEdge>) this.connectionsByNode.remove(removed);
                for (Entry<TrackNode, TrackEdge> entry : connections.entrySet()) {
                    TrackEdge trackEdge = (TrackEdge) entry.getValue();
                    EdgeData edgeData = trackEdge.getEdgeData();
                    for (TrackEdgePoint point : edgeData.getPoints()) {
                        if (level != null) {
                            point.invalidate(level);
                        }
                        this.edgePoints.remove(point.getType(), point.getId());
                    }
                    if (level != null) {
                        TrackNode otherNode = (TrackNode) entry.getKey();
                        for (TrackEdgeIntersection intersection : edgeData.getIntersections()) {
                            Couple<TrackNodeLocation> target = intersection.target;
                            TrackGraph graph = Create.RAILWAYS.getGraph(level, target.getFirst());
                            if (graph != null) {
                                graph.removeIntersection(intersection, removed, otherNode);
                            }
                        }
                    }
                }
                for (TrackNode railNode : connections.keySet()) {
                    if (this.connectionsByNode.containsKey(railNode)) {
                        ((Map) this.connectionsByNode.get(railNode)).remove(removed);
                    }
                }
                return true;
            }
        }
    }

    private void removeIntersection(TrackEdgeIntersection intersection, TrackNode targetNode1, TrackNode targetNode2) {
        TrackNode node1 = this.locateNode(intersection.target.getFirst());
        TrackNode node2 = this.locateNode(intersection.target.getSecond());
        if (node1 != null && node2 != null) {
            Map<TrackNode, TrackEdge> from1 = this.getConnectionsFrom(node1);
            if (from1 != null) {
                TrackEdge edge = (TrackEdge) from1.get(node2);
                if (edge != null) {
                    edge.getEdgeData().removeIntersection(this, intersection.id);
                }
            }
            Map<TrackNode, TrackEdge> from2 = this.getConnectionsFrom(node2);
            if (from2 != null) {
                TrackEdge edge = (TrackEdge) from2.get(node1);
                if (edge != null) {
                    edge.getEdgeData().removeIntersection(this, intersection.id);
                }
            }
        }
    }

    public static int nextNodeId() {
        return nodeNetIdGenerator.incrementAndGet();
    }

    public static int nextGraphId() {
        return graphNetIdGenerator.incrementAndGet();
    }

    public void transferAll(TrackGraph toOther) {
        this.nodes.forEach((loc, node) -> {
            if (toOther.addNodeIfAbsent(node)) {
                Create.RAILWAYS.sync.nodeAdded(toOther, node);
            }
        });
        this.connectionsByNode.forEach((node1, map) -> map.forEach((node2, edge) -> {
            TrackNode n1 = toOther.locateNode(node1.location);
            TrackNode n2 = toOther.locateNode(node2.location);
            if (n1 != null && n2 != null) {
                if (toOther.putConnection(n1, n2, edge)) {
                    Create.RAILWAYS.sync.edgeAdded(toOther, n1, n2, edge);
                    Create.RAILWAYS.sync.edgeDataChanged(toOther, n1, n2, edge);
                }
            }
        }));
        this.edgePoints.transferAll(toOther, toOther.edgePoints);
        this.nodes.clear();
        this.connectionsByNode.clear();
        toOther.invalidateBounds();
        Map<UUID, Train> trains = Create.RAILWAYS.trains;
        for (UUID uuid : trains.keySet()) {
            Train train = (Train) trains.get(uuid);
            if (train.graph == this) {
                train.graph = toOther;
            }
        }
    }

    public Set<TrackGraph> findDisconnectedGraphs(@Nullable LevelAccessor level, @Nullable Map<Integer, Pair<Integer, UUID>> splitSubGraphs) {
        Set<TrackGraph> dicovered = new HashSet();
        Set<TrackNodeLocation> vertices = new HashSet(this.nodes.keySet());
        List<TrackNodeLocation> frontier = new ArrayList();
        for (TrackGraph target = null; !vertices.isEmpty(); target = new TrackGraph()) {
            if (target != null) {
                dicovered.add(target);
            }
            TrackNodeLocation start = (TrackNodeLocation) vertices.stream().findFirst().get();
            frontier.add(start);
            vertices.remove(start);
            while (!frontier.isEmpty()) {
                TrackNodeLocation current = (TrackNodeLocation) frontier.remove(0);
                TrackNode currentNode = this.locateNode(current);
                Map<TrackNode, TrackEdge> connections = this.getConnectionsFrom(currentNode);
                for (TrackNode connected : connections.keySet()) {
                    if (vertices.remove(connected.getLocation())) {
                        frontier.add(connected.getLocation());
                    }
                }
                if (target != null) {
                    if (splitSubGraphs != null && splitSubGraphs.containsKey(currentNode.getNetId())) {
                        Pair<Integer, UUID> ids = (Pair<Integer, UUID>) splitSubGraphs.get(currentNode.getNetId());
                        target.setId(ids.getSecond());
                        target.netId = ids.getFirst();
                    }
                    this.transfer(level, currentNode, target);
                }
            }
            frontier.clear();
        }
        return dicovered;
    }

    public void setId(UUID id) {
        this.id = id;
        this.color = Color.rainbowColor(new Random(id.getLeastSignificantBits()).nextInt());
    }

    public void setNetId(int id) {
        this.netId = id;
    }

    public int getChecksum() {
        if (this.checksum == 0) {
            this.checksum = (Integer) this.nodes.values().stream().collect(Collectors.summingInt(TrackNode::getNetId));
        }
        return this.checksum;
    }

    public void transfer(LevelAccessor level, TrackNode node, TrackGraph target) {
        target.addNode(node);
        target.invalidateBounds();
        TrackNodeLocation nodeLoc = node.getLocation();
        Map<TrackNode, TrackEdge> connections = this.getConnectionsFrom(node);
        Map<UUID, Train> trains = Create.RAILWAYS.sided(level).trains;
        if (!connections.isEmpty()) {
            target.connectionsByNode.put(node, connections);
            for (TrackEdge entry : connections.values()) {
                EdgeData edgeData = entry.getEdgeData();
                for (TrackEdgePoint trackEdgePoint : edgeData.getPoints()) {
                    target.edgePoints.put(trackEdgePoint.getType(), trackEdgePoint);
                    this.edgePoints.remove(trackEdgePoint.getType(), trackEdgePoint.getId());
                }
            }
        }
        if (level != null) {
            for (UUID uuid : trains.keySet()) {
                Train train = (Train) trains.get(uuid);
                if (train.graph == this && train.isTravellingOn(node)) {
                    train.graph = target;
                }
            }
        }
        this.nodes.remove(nodeLoc);
        this.nodesById.remove(node.getNetId());
        this.connectionsByNode.remove(node);
        this.invalidateBounds();
    }

    public boolean isEmpty() {
        return this.nodes.isEmpty();
    }

    public Map<TrackNode, TrackEdge> getConnectionsFrom(TrackNode node) {
        return node == null ? null : (Map) this.connectionsByNode.getOrDefault(node, new HashMap());
    }

    public TrackEdge getConnection(Couple<TrackNode> nodes) {
        Map<TrackNode, TrackEdge> connectionsFrom = this.getConnectionsFrom(nodes.getFirst());
        return connectionsFrom == null ? null : (TrackEdge) connectionsFrom.get(nodes.getSecond());
    }

    public void connectNodes(LevelAccessor reader, TrackNodeLocation.DiscoveredLocation location, TrackNodeLocation.DiscoveredLocation location2, @Nullable BezierConnection turn) {
        TrackNode node1 = (TrackNode) this.nodes.get(location);
        TrackNode node2 = (TrackNode) this.nodes.get(location2);
        boolean bezier = turn != null;
        TrackMaterial material = bezier ? turn.getMaterial() : location2.materialA;
        TrackEdge edge = new TrackEdge(node1, node2, turn, material);
        TrackEdge edge2 = new TrackEdge(node2, node1, bezier ? turn.secondary() : null, material);
        for (TrackGraph graph : Create.RAILWAYS.trackNetworks.values()) {
            for (TrackNode otherNode1 : graph.nodes.values()) {
                Map<TrackNode, TrackEdge> connections = (Map<TrackNode, TrackEdge>) graph.connectionsByNode.get(otherNode1);
                if (connections != null) {
                    for (Entry<TrackNode, TrackEdge> entry : connections.entrySet()) {
                        TrackNode otherNode2 = (TrackNode) entry.getKey();
                        TrackEdge otherEdge = (TrackEdge) entry.getValue();
                        if ((graph != this || otherNode1 != node1 && otherNode2 != node1 && otherNode1 != node2 && otherNode2 != node2) && edge != otherEdge && (bezier || otherEdge.isTurn()) && (!otherEdge.isTurn() || !otherEdge.turn.isPrimary())) {
                            Collection<double[]> intersections = edge.getIntersection(node1, node2, otherEdge, otherNode1, otherNode2);
                            UUID id = UUID.randomUUID();
                            for (double[] intersection : intersections) {
                                double s = intersection[0];
                                double t = intersection[1];
                                edge.edgeData.addIntersection(this, id, s, otherNode1, otherNode2, t);
                                edge2.edgeData.addIntersection(this, id, edge.getLength() - s, otherNode1, otherNode2, t);
                                otherEdge.edgeData.addIntersection(graph, id, t, node1, node2, s);
                                TrackEdge otherEdge2 = graph.getConnection(Couple.create(otherNode2, otherNode1));
                                if (otherEdge2 != null) {
                                    otherEdge2.edgeData.addIntersection(graph, id, otherEdge.getLength() - t, node1, node2, s);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.putConnection(node1, node2, edge);
        this.putConnection(node2, node1, edge2);
        Create.RAILWAYS.sync.edgeAdded(this, node1, node2, edge);
        Create.RAILWAYS.sync.edgeAdded(this, node2, node1, edge2);
        this.markDirty();
    }

    public void disconnectNodes(TrackNode node1, TrackNode node2) {
        Map<TrackNode, TrackEdge> map1 = (Map<TrackNode, TrackEdge>) this.connectionsByNode.get(node1);
        Map<TrackNode, TrackEdge> map2 = (Map<TrackNode, TrackEdge>) this.connectionsByNode.get(node2);
        if (map1 != null) {
            map1.remove(node2);
        }
        if (map2 != null) {
            map2.remove(node1);
        }
    }

    public boolean putConnection(TrackNode node1, TrackNode node2, TrackEdge edge) {
        Map<TrackNode, TrackEdge> connections = (Map<TrackNode, TrackEdge>) this.connectionsByNode.computeIfAbsent(node1, n -> new IdentityHashMap());
        return connections.containsKey(node2) && ((TrackEdge) connections.get(node2)).getEdgeData().hasPoints() ? false : connections.put(node2, edge) == null;
    }

    public float distanceToLocationSqr(Level level, Vec3 location) {
        float nearest = Float.MAX_VALUE;
        for (TrackNodeLocation tnl : this.nodes.keySet()) {
            if (Objects.equals(tnl.dimension, level.dimension())) {
                nearest = Math.min(nearest, (float) tnl.getLocation().distanceToSqr(location));
            }
        }
        return nearest;
    }

    public void deferIntersectionUpdate(TrackEdge edge) {
        this.deferredIntersectionUpdates.add(edge);
    }

    public void resolveIntersectingEdgeGroups(Level level) {
        for (TrackEdge edge : this.deferredIntersectionUpdates) {
            if (this.connectionsByNode.containsKey(edge.node1) && edge == ((Map) this.connectionsByNode.get(edge.node1)).get(edge.node2)) {
                EdgeData edgeData = edge.getEdgeData();
                for (TrackEdgeIntersection intersection : edgeData.getIntersections()) {
                    UUID groupId = edgeData.getGroupAtPosition(this, intersection.location);
                    Couple<TrackNodeLocation> target = intersection.target;
                    TrackGraph graph = Create.RAILWAYS.getGraph(level, target.getFirst());
                    if (graph != null) {
                        TrackNode node1 = graph.locateNode(target.getFirst());
                        TrackNode node2 = graph.locateNode(target.getSecond());
                        Map<TrackNode, TrackEdge> connectionsFrom = graph.getConnectionsFrom(node1);
                        if (connectionsFrom != null) {
                            TrackEdge otherEdge = (TrackEdge) connectionsFrom.get(node2);
                            if (otherEdge != null) {
                                UUID otherGroupId = otherEdge.getEdgeData().getGroupAtPosition(graph, intersection.targetLocation);
                                SignalEdgeGroup group = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(groupId);
                                SignalEdgeGroup otherGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(otherGroupId);
                                if (group != null && otherGroup != null) {
                                    intersection.groupId = groupId;
                                    group.putIntersection(intersection.id, otherGroupId);
                                    otherGroup.putIntersection(intersection.id, groupId);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.deferredIntersectionUpdates.clear();
    }

    public void markDirty() {
        Create.RAILWAYS.markTracksDirty();
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", this.id);
        tag.putInt("Color", this.color.getRGB());
        Map<TrackNode, Integer> indexTracker = new HashMap();
        ListTag nodesList = new ListTag();
        int i = 0;
        for (TrackNode railNode : this.nodes.values()) {
            indexTracker.put(railNode, i);
            CompoundTag nodeTag = new CompoundTag();
            nodeTag.put("Location", railNode.getLocation().write(dimensions));
            nodeTag.put("Normal", VecHelper.writeNBT(railNode.getNormal()));
            nodesList.add(nodeTag);
            i++;
        }
        this.connectionsByNode.forEach((node1, map) -> {
            Integer index1 = (Integer) indexTracker.get(node1);
            if (index1 != null) {
                CompoundTag nodeTagx = (CompoundTag) nodesList.get(index1);
                ListTag connectionsList = new ListTag();
                map.forEach((node2, edge) -> {
                    CompoundTag connectionTag = new CompoundTag();
                    Integer index2 = (Integer) indexTracker.get(node2);
                    if (index2 != null) {
                        connectionTag.putInt("To", index2);
                        connectionTag.put("EdgeData", edge.write(dimensions));
                        connectionsList.add(connectionTag);
                    }
                });
                nodeTagx.put("Connections", connectionsList);
            }
        });
        tag.put("Nodes", nodesList);
        tag.put("Points", this.edgePoints.write(dimensions));
        return tag;
    }

    public static TrackGraph read(CompoundTag tag, DimensionPalette dimensions) {
        TrackGraph graph = new TrackGraph(tag.getUUID("Id"));
        graph.color = new Color(tag.getInt("Color"));
        graph.edgePoints.read(tag.getCompound("Points"), dimensions);
        Map<Integer, TrackNode> indexTracker = new HashMap();
        ListTag nodesList = tag.getList("Nodes", 10);
        int i = 0;
        for (Tag t : nodesList) {
            CompoundTag nodeTag = (CompoundTag) t;
            TrackNodeLocation location = TrackNodeLocation.read(nodeTag.getCompound("Location"), dimensions);
            Vec3 normal = VecHelper.readNBT(nodeTag.getList("Normal", 6));
            graph.loadNode(location, nextNodeId(), normal);
            indexTracker.put(i, graph.locateNode(location));
            i++;
        }
        i = 0;
        for (Tag t : nodesList) {
            CompoundTag nodeTag = (CompoundTag) t;
            TrackNode node1 = (TrackNode) indexTracker.get(i);
            i++;
            if (nodeTag.contains("Connections")) {
                NBTHelper.iterateCompoundList(nodeTag.getList("Connections", 10), c -> {
                    TrackNode node2 = (TrackNode) indexTracker.get(c.getInt("To"));
                    TrackEdge edge = TrackEdge.read(node1, node2, c.getCompound("EdgeData"), graph, dimensions);
                    graph.putConnection(node1, node2, edge);
                });
            }
        }
        return graph;
    }
}