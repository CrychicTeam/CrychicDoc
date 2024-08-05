package com.simibubi.create.content.trains.graph;

import com.simibubi.create.AllKeys;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Pair;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TrackGraphVisualizer {

    public static void visualiseSignalEdgeGroups(TrackGraph graph) {
        Minecraft mc = Minecraft.getInstance();
        Entity cameraEntity = mc.cameraEntity;
        if (cameraEntity != null) {
            AABB box = graph.getBounds(mc.level).box;
            if (box != null && box.intersects(cameraEntity.getBoundingBox().inflate(50.0))) {
                Vec3 camera = cameraEntity.getEyePosition();
                Outliner outliner = CreateClient.OUTLINER;
                Map<UUID, SignalEdgeGroup> allGroups = Create.RAILWAYS.sided(null).signalEdgeGroups;
                float width = 0.125F;
                for (Entry<TrackNodeLocation, TrackNode> nodeEntry : graph.nodes.entrySet()) {
                    TrackNodeLocation nodeLocation = (TrackNodeLocation) nodeEntry.getKey();
                    TrackNode node = (TrackNode) nodeEntry.getValue();
                    if (nodeLocation != null) {
                        Vec3 location = nodeLocation.getLocation();
                        if (!(location.distanceTo(camera) > 50.0) && mc.level.m_46472_().equals(nodeLocation.dimension)) {
                            Map<TrackNode, TrackEdge> map = (Map<TrackNode, TrackEdge>) graph.connectionsByNode.get(node);
                            if (map != null) {
                                int hashCode = node.hashCode();
                                for (Entry<TrackNode, TrackEdge> entry : map.entrySet()) {
                                    TrackNode other = (TrackNode) entry.getKey();
                                    TrackEdge edge = (TrackEdge) entry.getValue();
                                    EdgeData signalData = edge.getEdgeData();
                                    if (edge.node1.location.dimension.equals(edge.node2.location.dimension) && (other.hashCode() <= hashCode || !(other.location.getLocation().distanceTo(camera) <= 50.0))) {
                                        Vec3 yOffset = new Vec3(0.0, (double) ((float) (other.hashCode() > hashCode ? 6 : 5) / 64.0F), 0.0);
                                        Vec3 startPoint = edge.getPosition(graph, 0.0);
                                        Vec3 endPoint = edge.getPosition(graph, 1.0);
                                        if (!edge.isTurn()) {
                                            if (signalData.hasSignalBoundaries()) {
                                                double prev = 0.0;
                                                double length = edge.getLength();
                                                SignalBoundary prevBoundary = null;
                                                SignalEdgeGroup group = null;
                                                for (TrackEdgePoint trackEdgePoint : signalData.getPoints()) {
                                                    if (trackEdgePoint instanceof SignalBoundary) {
                                                        SignalBoundary boundary = (SignalBoundary) trackEdgePoint;
                                                        prevBoundary = boundary;
                                                        group = (SignalEdgeGroup) allGroups.get(boundary.getGroup(node));
                                                        if (group != null) {
                                                            outliner.showLine(Pair.of(boundary, edge), edge.getPosition(graph, prev + (prev == 0.0 ? 0.0 : 0.0625 / length)).add(yOffset), edge.getPosition(graph, (prev = boundary.getLocationOn(edge) / length) - 0.0625 / length).add(yOffset)).colored(group.color.get()).lineWidth(width);
                                                        }
                                                    }
                                                }
                                                if (prevBoundary != null) {
                                                    group = (SignalEdgeGroup) allGroups.get(prevBoundary.getGroup(other));
                                                    if (group != null) {
                                                        outliner.showLine(edge, edge.getPosition(graph, prev + 0.0625 / length).add(yOffset), endPoint.add(yOffset)).colored(group.color.get()).lineWidth(width);
                                                    }
                                                    continue;
                                                }
                                            }
                                            UUID singleGroup = signalData.getEffectiveEdgeGroupId(graph);
                                            SignalEdgeGroup singleEdgeGroup = singleGroup == null ? null : (SignalEdgeGroup) allGroups.get(singleGroup);
                                            if (singleEdgeGroup != null) {
                                                outliner.showLine(edge, startPoint.add(yOffset), endPoint.add(yOffset)).colored(singleEdgeGroup.color.get()).lineWidth(width);
                                            }
                                        } else {
                                            if (signalData.hasSignalBoundaries()) {
                                                Iterator<TrackEdgePoint> points = signalData.getPoints().iterator();
                                                SignalBoundary currentBoundary = null;
                                                double currentBoundaryPosition = 0.0;
                                                while (points.hasNext()) {
                                                    TrackEdgePoint next = (TrackEdgePoint) points.next();
                                                    if (next instanceof SignalBoundary signal) {
                                                        currentBoundary = signal;
                                                        currentBoundaryPosition = signal.getLocationOn(edge);
                                                        break;
                                                    }
                                                }
                                                if (currentBoundary == null) {
                                                    continue;
                                                }
                                                UUID initialGroupId = currentBoundary.getGroup(node);
                                                if (initialGroupId == null) {
                                                    continue;
                                                }
                                                SignalEdgeGroup initialGroup = (SignalEdgeGroup) allGroups.get(initialGroupId);
                                                if (initialGroup == null) {
                                                    continue;
                                                }
                                                Color currentColour = initialGroup.color.get();
                                                Vec3 previous = null;
                                                BezierConnection turn = edge.getTurn();
                                                for (int i = 0; i <= turn.getSegmentCount(); i++) {
                                                    double f = (double) ((float) i * 1.0F / (float) turn.getSegmentCount());
                                                    double position = f * turn.getLength();
                                                    Vec3 current = edge.getPosition(graph, f);
                                                    if (previous != null) {
                                                        if (currentBoundary != null && position > currentBoundaryPosition) {
                                                            Vec3 var59 = edge.getPosition(graph, (currentBoundaryPosition - (double) width) / turn.getLength());
                                                            outliner.showLine(Pair.of(edge, previous), previous.add(yOffset), var59.add(yOffset)).colored(currentColour).lineWidth(width);
                                                            current = edge.getPosition(graph, (currentBoundaryPosition + (double) width) / turn.getLength());
                                                            previous = current;
                                                            UUID newId = currentBoundary.getGroup(other);
                                                            if (newId != null && allGroups.containsKey(newId)) {
                                                                currentColour = ((SignalEdgeGroup) allGroups.get(newId)).color.get();
                                                            }
                                                            currentBoundary = null;
                                                            while (points.hasNext()) {
                                                                TrackEdgePoint next = (TrackEdgePoint) points.next();
                                                                if (next instanceof SignalBoundary signal) {
                                                                    currentBoundary = signal;
                                                                    currentBoundaryPosition = signal.getLocationOn(edge);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        outliner.showLine(Pair.of(edge, previous), previous.add(yOffset), current.add(yOffset)).colored(currentColour).lineWidth(width);
                                                    }
                                                    previous = current;
                                                }
                                            }
                                            UUID singleGroup = signalData.getEffectiveEdgeGroupId(graph);
                                            SignalEdgeGroup singleEdgeGroup = singleGroup == null ? null : (SignalEdgeGroup) allGroups.get(singleGroup);
                                            if (singleEdgeGroup != null) {
                                                Vec3 previous = null;
                                                BezierConnection turn = edge.getTurn();
                                                for (int i = 0; i <= turn.getSegmentCount(); i++) {
                                                    Vec3 current = edge.getPosition(graph, (double) ((float) i * 1.0F / (float) turn.getSegmentCount()));
                                                    if (previous != null) {
                                                        outliner.showLine(Pair.of(edge, previous), previous.add(yOffset), current.add(yOffset)).colored(singleEdgeGroup.color.get()).lineWidth(width);
                                                    }
                                                    previous = current;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void debugViewGraph(TrackGraph graph, boolean extended) {
        Minecraft mc = Minecraft.getInstance();
        Entity cameraEntity = mc.cameraEntity;
        if (cameraEntity != null) {
            AABB box = graph.getBounds(mc.level).box;
            if (box != null && box.intersects(cameraEntity.getBoundingBox().inflate(50.0))) {
                Vec3 camera = cameraEntity.getEyePosition();
                for (Entry<TrackNodeLocation, TrackNode> nodeEntry : graph.nodes.entrySet()) {
                    TrackNodeLocation nodeLocation = (TrackNodeLocation) nodeEntry.getKey();
                    TrackNode node = (TrackNode) nodeEntry.getValue();
                    if (nodeLocation != null) {
                        Vec3 location = nodeLocation.getLocation();
                        if (!(location.distanceTo(camera) > 50.0) && mc.level.m_46472_().equals(nodeLocation.dimension)) {
                            Vec3 yOffset = new Vec3(0.0, 0.1875, 0.0);
                            Vec3 v1 = location.add(yOffset);
                            Vec3 v2 = v1.add(node.normal.scale(0.1875));
                            CreateClient.OUTLINER.showLine(node.netId, v1, v2).colored(Color.mixColors(Color.WHITE, graph.color, 1.0F)).lineWidth(0.125F);
                            Map<TrackNode, TrackEdge> map = (Map<TrackNode, TrackEdge>) graph.connectionsByNode.get(node);
                            if (map != null) {
                                int hashCode = node.hashCode();
                                for (Entry<TrackNode, TrackEdge> entry : map.entrySet()) {
                                    TrackNode other = (TrackNode) entry.getKey();
                                    TrackEdge edge = (TrackEdge) entry.getValue();
                                    if (!edge.node1.location.dimension.equals(edge.node2.location.dimension)) {
                                        v1 = location.add(yOffset);
                                        v2 = v1.add(node.normal.scale(0.1875));
                                        CreateClient.OUTLINER.showLine(node.netId, v1, v2).colored(Color.mixColors(Color.WHITE, graph.color, 1.0F)).lineWidth(0.25F);
                                    } else if (other.hashCode() <= hashCode || AllKeys.isKeyDown(341)) {
                                        yOffset = new Vec3(0.0, (double) ((float) (other.hashCode() > hashCode ? 6 : 4) / 16.0F), 0.0);
                                        if (!edge.isTurn()) {
                                            if (extended) {
                                                Vec3 materialPos = edge.getPosition(graph, 0.5).add(0.0, 1.0, 0.0);
                                                CreateClient.OUTLINER.showItem(Pair.of(edge, edge.edgeData), materialPos, edge.getTrackMaterial().asStack());
                                                CreateClient.OUTLINER.showAABB(edge.edgeData, AABB.ofSize(materialPos, 0.25, 0.0, 0.25).move(0.0, -0.5, 0.0)).lineWidth(0.0625F).colored(graph.color);
                                            }
                                            CreateClient.OUTLINER.showLine(edge, edge.getPosition(graph, 0.0).add(yOffset), edge.getPosition(graph, 1.0).add(yOffset)).colored(graph.color).lineWidth(0.0625F);
                                        } else {
                                            Vec3 previous = null;
                                            BezierConnection turn = edge.getTurn();
                                            if (extended) {
                                                Vec3 materialPos = edge.getPosition(graph, 0.5).add(0.0, 1.0, 0.0);
                                                CreateClient.OUTLINER.showItem(Pair.of(edge, edge.edgeData), materialPos, edge.getTrackMaterial().asStack());
                                                CreateClient.OUTLINER.showAABB(edge.edgeData, AABB.ofSize(materialPos, 0.25, 0.0, 0.25).move(0.0, -0.5, 0.0)).lineWidth(0.0625F).colored(graph.color);
                                            }
                                            for (int i = 0; i <= turn.getSegmentCount(); i++) {
                                                Vec3 current = edge.getPosition(graph, (double) ((float) i * 1.0F / (float) turn.getSegmentCount()));
                                                if (previous != null) {
                                                    CreateClient.OUTLINER.showLine(Pair.of(edge, previous), previous.add(yOffset), current.add(yOffset)).colored(graph.color).lineWidth(0.0625F);
                                                }
                                                previous = current;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}