package com.simibubi.create.content.trains.graph;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class TrackGraphSyncPacket extends TrackGraphPacket {

    Map<Integer, Pair<TrackNodeLocation, Vec3>> addedNodes;

    List<Pair<Pair<Couple<Integer>, TrackMaterial>, BezierConnection>> addedEdges;

    List<Integer> removedNodes;

    List<TrackEdgePoint> addedEdgePoints;

    List<UUID> removedEdgePoints;

    Map<Integer, Pair<Integer, UUID>> splitSubGraphs;

    Map<Couple<Integer>, Pair<Integer, List<UUID>>> updatedEdgeData;

    boolean fullWipe;

    static final int NULL_GROUP = 0;

    static final int PASSIVE_GROUP = 1;

    static final int GROUP = 2;

    public TrackGraphSyncPacket(UUID graphId, int netId) {
        this.graphId = graphId;
        this.netId = netId;
        this.addedNodes = new HashMap();
        this.addedEdges = new ArrayList();
        this.removedNodes = new ArrayList();
        this.addedEdgePoints = new ArrayList();
        this.removedEdgePoints = new ArrayList();
        this.updatedEdgeData = new HashMap();
        this.splitSubGraphs = new HashMap();
        this.packetDeletesGraph = false;
    }

    public TrackGraphSyncPacket(FriendlyByteBuf buffer) {
        this.graphId = buffer.readUUID();
        this.netId = buffer.readInt();
        this.packetDeletesGraph = buffer.readBoolean();
        this.fullWipe = buffer.readBoolean();
        if (!this.packetDeletesGraph) {
            DimensionPalette dimensions = DimensionPalette.receive(buffer);
            this.addedNodes = new HashMap();
            this.addedEdges = new ArrayList();
            this.addedEdgePoints = new ArrayList();
            this.removedEdgePoints = new ArrayList();
            this.removedNodes = new ArrayList();
            this.splitSubGraphs = new HashMap();
            this.updatedEdgeData = new HashMap();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.removedNodes.add(buffer.readVarInt());
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.addedNodes.put(buffer.readVarInt(), Pair.of(TrackNodeLocation.receive(buffer, dimensions), VecHelper.read(buffer)));
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.addedEdges.add(Pair.of(Pair.of(Couple.create(buffer::m_130242_), TrackMaterial.deserialize(buffer.readUtf())), buffer.readBoolean() ? new BezierConnection(buffer) : null));
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.addedEdgePoints.add(EdgePointType.read(buffer, dimensions));
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.removedEdgePoints.add(buffer.readUUID());
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ArrayList<UUID> list = new ArrayList();
                Couple<Integer> key = Couple.create(buffer::readInt);
                Pair<Integer, List<UUID>> entry = Pair.of(buffer.readVarInt(), list);
                int size2 = buffer.readVarInt();
                for (int j = 0; j < size2; j++) {
                    list.add(buffer.readUUID());
                }
                this.updatedEdgeData.put(key, entry);
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                this.splitSubGraphs.put(buffer.readVarInt(), Pair.of(buffer.readInt(), buffer.readUUID()));
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.graphId);
        buffer.writeInt(this.netId);
        buffer.writeBoolean(this.packetDeletesGraph);
        buffer.writeBoolean(this.fullWipe);
        if (!this.packetDeletesGraph) {
            DimensionPalette dimensions = new DimensionPalette();
            this.addedNodes.forEach((node, loc) -> dimensions.encode(((TrackNodeLocation) loc.getFirst()).dimension));
            this.addedEdgePoints.forEach(ep -> ep.edgeLocation.forEach(loc -> dimensions.encode(loc.dimension)));
            dimensions.send(buffer);
            buffer.writeVarInt(this.removedNodes.size());
            this.removedNodes.forEach(buffer::m_130130_);
            buffer.writeVarInt(this.addedNodes.size());
            this.addedNodes.forEach((node, loc) -> {
                buffer.writeVarInt(node);
                ((TrackNodeLocation) loc.getFirst()).send(buffer, dimensions);
                VecHelper.write((Vec3) loc.getSecond(), buffer);
            });
            buffer.writeVarInt(this.addedEdges.size());
            this.addedEdges.forEach(pairx -> {
                ((Couple) ((Pair) pairx.getFirst()).getFirst()).forEach(buffer::m_130130_);
                buffer.writeUtf(((TrackMaterial) ((Pair) pairx.getFirst()).getSecond()).id.toString());
                BezierConnection turn = (BezierConnection) pairx.getSecond();
                buffer.writeBoolean(turn != null);
                if (turn != null) {
                    turn.write(buffer);
                }
            });
            buffer.writeVarInt(this.addedEdgePoints.size());
            this.addedEdgePoints.forEach(ep -> ep.write(buffer, dimensions));
            buffer.writeVarInt(this.removedEdgePoints.size());
            this.removedEdgePoints.forEach(buffer::m_130077_);
            buffer.writeVarInt(this.updatedEdgeData.size());
            for (Entry<Couple<Integer>, Pair<Integer, List<UUID>>> entry : this.updatedEdgeData.entrySet()) {
                ((Couple) entry.getKey()).forEach(buffer::writeInt);
                Pair<Integer, List<UUID>> pair = (Pair<Integer, List<UUID>>) entry.getValue();
                buffer.writeVarInt(pair.getFirst());
                List<UUID> list = pair.getSecond();
                buffer.writeVarInt(list.size());
                list.forEach(buffer::m_130077_);
            }
            buffer.writeVarInt(this.splitSubGraphs.size());
            this.splitSubGraphs.forEach((node, p) -> {
                buffer.writeVarInt(node);
                buffer.writeInt((Integer) p.getFirst());
                buffer.writeUUID((UUID) p.getSecond());
            });
        }
    }

    @Override
    protected void handle(GlobalRailwayManager manager, TrackGraph graph) {
        if (this.packetDeletesGraph) {
            manager.removeGraph(graph);
        } else {
            if (this.fullWipe) {
                manager.removeGraph(graph);
                graph = Create.RAILWAYS.sided(null).getOrCreateGraph(this.graphId, this.netId);
            }
            for (int nodeId : this.removedNodes) {
                TrackNode node = graph.getNode(nodeId);
                if (node != null) {
                    graph.removeNode(null, node.getLocation());
                }
            }
            for (Entry<Integer, Pair<TrackNodeLocation, Vec3>> entry : this.addedNodes.entrySet()) {
                Integer nodeIdx = (Integer) entry.getKey();
                Pair<TrackNodeLocation, Vec3> nodeLocation = (Pair<TrackNodeLocation, Vec3>) entry.getValue();
                graph.loadNode(nodeLocation.getFirst(), nodeIdx, nodeLocation.getSecond());
            }
            for (Pair<Pair<Couple<Integer>, TrackMaterial>, BezierConnection> pair : this.addedEdges) {
                Couple<TrackNode> nodes = pair.getFirst().getFirst().map(graph::getNode);
                TrackNode node1 = nodes.getFirst();
                TrackNode node2 = nodes.getSecond();
                if (node1 != null && node2 != null) {
                    graph.putConnection(node1, node2, new TrackEdge(node1, node2, pair.getSecond(), pair.getFirst().getSecond()));
                }
            }
            for (TrackEdgePoint edgePoint : this.addedEdgePoints) {
                graph.edgePoints.put(edgePoint.getType(), edgePoint);
            }
            for (UUID uuid : this.removedEdgePoints) {
                for (EdgePointType<?> type : EdgePointType.TYPES.values()) {
                    graph.edgePoints.remove(type, uuid);
                }
            }
            this.handleEdgeData(manager, graph);
            if (!this.splitSubGraphs.isEmpty()) {
                graph.findDisconnectedGraphs(null, this.splitSubGraphs).forEach(manager::putGraph);
            }
        }
    }

    protected void handleEdgeData(GlobalRailwayManager manager, TrackGraph graph) {
        for (Entry<Couple<Integer>, Pair<Integer, List<UUID>>> entry : this.updatedEdgeData.entrySet()) {
            List<UUID> idList = (List<UUID>) ((Pair) entry.getValue()).getSecond();
            int groupType = (Integer) ((Pair) entry.getValue()).getFirst();
            Couple<TrackNode> nodes = ((Couple) entry.getKey()).map(graph::getNode);
            if (!nodes.either(Objects::isNull)) {
                TrackEdge edge = (TrackEdge) graph.getConnectionsFrom(nodes.getFirst()).get(nodes.getSecond());
                if (edge != null) {
                    EdgeData edgeData = new EdgeData(edge);
                    if (groupType == 0) {
                        edgeData.setSingleSignalGroup(null, null);
                    } else if (groupType == 1) {
                        edgeData.setSingleSignalGroup(null, EdgeData.passiveGroup);
                    } else {
                        edgeData.setSingleSignalGroup(null, (UUID) idList.get(0));
                    }
                    List<TrackEdgePoint> points = edgeData.getPoints();
                    edge.edgeData = edgeData;
                    for (int i = groupType == 2 ? 1 : 0; i < idList.size(); i++) {
                        UUID uuid = (UUID) idList.get(i);
                        for (EdgePointType<?> type : EdgePointType.TYPES.values()) {
                            TrackEdgePoint point = graph.edgePoints.get((EdgePointType<TrackEdgePoint>) type, uuid);
                            if (point != null) {
                                points.add(point);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void syncEdgeData(TrackNode node1, TrackNode node2, TrackEdge edge) {
        Couple<Integer> key = Couple.create(node1.getNetId(), node2.getNetId());
        List<UUID> list = new ArrayList();
        EdgeData edgeData = edge.getEdgeData();
        int groupType = edgeData.hasSignalBoundaries() ? 0 : (EdgeData.passiveGroup.equals(edgeData.getSingleSignalGroup()) ? 1 : 2);
        if (groupType == 2) {
            list.add(edgeData.getSingleSignalGroup());
        }
        for (TrackEdgePoint point : edgeData.getPoints()) {
            list.add(point.getId());
        }
        this.updatedEdgeData.put(key, Pair.of(groupType, list));
    }
}