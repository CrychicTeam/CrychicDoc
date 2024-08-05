package com.simibubi.create.content.trains.track;

import com.simibubi.create.Create;
import com.simibubi.create.api.event.TrackGraphMergeEvent;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackGraphSync;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.signal.SignalPropagator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class TrackPropagator {

    public static void onRailRemoved(LevelAccessor reader, BlockPos pos, BlockState state) {
        if (state.m_60734_() instanceof ITrackBlock track) {
            Collection var13 = track.getConnected(reader, pos, state, false, null);
            GlobalRailwayManager manager = Create.RAILWAYS;
            TrackGraphSync sync = manager.sync;
            for (TrackNodeLocation.DiscoveredLocation removedLocation : var13) {
                for (TrackGraph foundGraph : manager.getGraphs(reader, removedLocation)) {
                    TrackNode removedNode = foundGraph.locateNode(removedLocation);
                    if (removedNode != null) {
                        foundGraph.removeNode(reader, removedLocation);
                        sync.nodeRemoved(foundGraph, removedNode);
                        if (foundGraph.isEmpty()) {
                            manager.removeGraphAndGroup(foundGraph);
                            sync.graphRemoved(foundGraph);
                        }
                    }
                }
            }
            Set<BlockPos> positionsToUpdate = new HashSet();
            for (TrackNodeLocation.DiscoveredLocation removedEnd : var13) {
                positionsToUpdate.addAll(removedEnd.allAdjacent());
            }
            Set<TrackGraph> toUpdate = new HashSet();
            for (BlockPos blockPos : positionsToUpdate) {
                if (!blockPos.equals(pos)) {
                    TrackGraph onRailAdded = onRailAdded(reader, blockPos, reader.m_8055_(blockPos));
                    if (onRailAdded != null) {
                        toUpdate.add(onRailAdded);
                    }
                }
            }
            for (TrackGraph railGraph : toUpdate) {
                manager.updateSplitGraph(reader, railGraph);
            }
            manager.markTracksDirty();
        }
    }

    public static TrackGraph onRailAdded(LevelAccessor reader, BlockPos pos, BlockState state) {
        if (!(state.m_60734_() instanceof ITrackBlock track)) {
            return null;
        } else {
            GlobalRailwayManager manager = Create.RAILWAYS;
            TrackGraphSync sync = manager.sync;
            ArrayList frontier = new ArrayList();
            HashSet visited = new HashSet();
            HashSet connectedGraphs = new HashSet();
            addInitialEndsOf(reader, pos, state, track, frontier, false);
            int emergencyExit = 1000;
            while (!frontier.isEmpty() && emergencyExit-- != 0) {
                TrackPropagator.FrontierEntry entry = (TrackPropagator.FrontierEntry) frontier.remove(0);
                List<TrackGraph> intersecting = manager.getGraphs(reader, entry.currentNode);
                for (TrackGraph graph : intersecting) {
                    TrackNode node = graph.locateNode(entry.currentNode);
                    graph.removeNode(reader, entry.currentNode);
                    sync.nodeRemoved(graph, node);
                    connectedGraphs.add(graph);
                }
                if (intersecting.isEmpty()) {
                    Collection<TrackNodeLocation.DiscoveredLocation> ends = ITrackBlock.walkConnectedTracks(reader, entry.currentNode, false);
                    if (entry.prevNode != null) {
                        ends.remove(entry.prevNode);
                    }
                    continueSearch(frontier, visited, entry, ends);
                }
            }
            frontier.clear();
            visited.clear();
            TrackGraph graph = null;
            Iterator<TrackGraph> iterator = connectedGraphs.iterator();
            while (iterator.hasNext()) {
                TrackGraph railGraph = (TrackGraph) iterator.next();
                if (railGraph.isEmpty() && connectedGraphs.size() != 1) {
                    manager.removeGraphAndGroup(railGraph);
                    sync.graphRemoved(railGraph);
                    iterator.remove();
                }
            }
            if (connectedGraphs.size() > 1) {
                for (TrackGraph other : connectedGraphs) {
                    if (graph == null) {
                        graph = other;
                    } else {
                        MinecraftForge.EVENT_BUS.post(new TrackGraphMergeEvent(other, graph));
                        other.transferAll(graph);
                        manager.removeGraphAndGroup(other);
                        sync.graphRemoved(other);
                    }
                }
            } else if (connectedGraphs.size() == 1) {
                graph = (TrackGraph) connectedGraphs.stream().findFirst().get();
            } else {
                manager.putGraphWithDefaultGroup(graph = new TrackGraph());
            }
            TrackNodeLocation.DiscoveredLocation startNode = null;
            addInitialEndsOf(reader, pos, state, track, frontier, true);
            emergencyExit = 1000;
            while (!frontier.isEmpty() && emergencyExit-- != 0) {
                TrackPropagator.FrontierEntry entry = (TrackPropagator.FrontierEntry) frontier.remove(0);
                Collection<TrackNodeLocation.DiscoveredLocation> ends = ITrackBlock.walkConnectedTracks(reader, entry.currentNode, false);
                boolean first = entry.prevNode == null;
                if (!first) {
                    ends.remove(entry.prevNode);
                }
                if (isValidGraphNodeLocation(entry.currentNode, ends, first)) {
                    startNode = entry.currentNode;
                    break;
                }
                continueSearch(frontier, visited, entry, ends);
            }
            frontier.clear();
            Set<TrackNode> addedNodes = new HashSet();
            graph.createNodeIfAbsent(startNode);
            frontier.add(new TrackPropagator.FrontierEntry(startNode, null, startNode));
            emergencyExit = 1000;
            while (!frontier.isEmpty() && emergencyExit-- != 0) {
                TrackPropagator.FrontierEntry entryx = (TrackPropagator.FrontierEntry) frontier.remove(0);
                TrackNodeLocation.DiscoveredLocation parentNode = entryx.parentNode;
                Collection<TrackNodeLocation.DiscoveredLocation> endsx = ITrackBlock.walkConnectedTracks(reader, entryx.currentNode, false);
                boolean firstx = entryx.prevNode == null;
                if (!firstx) {
                    endsx.remove(entryx.prevNode);
                }
                if (isValidGraphNodeLocation(entryx.currentNode, endsx, firstx) && entryx.currentNode != startNode) {
                    boolean nodeIsNew = graph.createNodeIfAbsent(entryx.currentNode);
                    graph.connectNodes(reader, parentNode, entryx.currentNode, entryx.currentNode.getTurn());
                    addedNodes.add(graph.locateNode(entryx.currentNode));
                    parentNode = entryx.currentNode;
                    if (!nodeIsNew) {
                        continue;
                    }
                }
                continueSearchWithParent(frontier, entryx, parentNode, endsx);
            }
            manager.markTracksDirty();
            for (TrackNode trackNode : addedNodes) {
                SignalPropagator.notifySignalsOfNewNode(graph, trackNode);
            }
            return graph;
        }
    }

    private static void addInitialEndsOf(LevelAccessor reader, BlockPos pos, BlockState state, ITrackBlock track, List<TrackPropagator.FrontierEntry> frontier, boolean ignoreTurns) {
        for (TrackNodeLocation.DiscoveredLocation initial : track.getConnected(reader, pos, state, ignoreTurns, null)) {
            frontier.add(new TrackPropagator.FrontierEntry(null, null, initial));
        }
    }

    private static void continueSearch(List<TrackPropagator.FrontierEntry> frontier, Set<TrackNodeLocation.DiscoveredLocation> visited, TrackPropagator.FrontierEntry entry, Collection<TrackNodeLocation.DiscoveredLocation> ends) {
        for (TrackNodeLocation.DiscoveredLocation location : ends) {
            if (visited.add(location)) {
                frontier.add(new TrackPropagator.FrontierEntry(null, entry.currentNode, location));
            }
        }
    }

    private static void continueSearchWithParent(List<TrackPropagator.FrontierEntry> frontier, TrackPropagator.FrontierEntry entry, TrackNodeLocation.DiscoveredLocation parentNode, Collection<TrackNodeLocation.DiscoveredLocation> ends) {
        for (TrackNodeLocation.DiscoveredLocation location : ends) {
            frontier.add(new TrackPropagator.FrontierEntry(parentNode, entry.currentNode, location));
        }
    }

    public static boolean isValidGraphNodeLocation(TrackNodeLocation.DiscoveredLocation location, Collection<TrackNodeLocation.DiscoveredLocation> next, boolean first) {
        int size = next.size() - (first ? 1 : 0);
        if (size != 1) {
            return true;
        } else if (location.shouldForceNode()) {
            return true;
        } else if (location.differentMaterials()) {
            return true;
        } else if (next.stream().anyMatch(TrackNodeLocation.DiscoveredLocation::shouldForceNode)) {
            return true;
        } else {
            Vec3 direction = location.getDirection();
            if (direction != null && next.stream().anyMatch(dl -> dl.notInLineWith(direction))) {
                return true;
            } else {
                Vec3 vec = location.getLocation();
                boolean centeredX = !Mth.equal(vec.x, (double) Math.round(vec.x));
                boolean centeredZ = !Mth.equal(vec.z, (double) Math.round(vec.z));
                return centeredX && !centeredZ ? (int) Math.round(vec.z) % 16 == 0 : (int) Math.round(vec.x) % 16 == 0;
            }
        }
    }

    static class FrontierEntry {

        TrackNodeLocation.DiscoveredLocation prevNode;

        TrackNodeLocation.DiscoveredLocation currentNode;

        TrackNodeLocation.DiscoveredLocation parentNode;

        public FrontierEntry(TrackNodeLocation.DiscoveredLocation parent, TrackNodeLocation.DiscoveredLocation previousNode, TrackNodeLocation.DiscoveredLocation location) {
            this.parentNode = parent;
            this.prevNode = previousNode;
            this.currentNode = location;
        }
    }
}