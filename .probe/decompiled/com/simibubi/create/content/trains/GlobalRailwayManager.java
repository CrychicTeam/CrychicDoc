package com.simibubi.create.content.trains;

import com.simibubi.create.AllPackets;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.content.trains.display.GlobalTrainDisplayData;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainPacket;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackGraphSync;
import com.simibubi.create.content.trains.graph.TrackGraphVisualizer;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableObject;

public class GlobalRailwayManager {

    public Map<UUID, TrackGraph> trackNetworks;

    public Map<UUID, SignalEdgeGroup> signalEdgeGroups;

    public Map<UUID, Train> trains;

    public TrackGraphSync sync;

    private List<Train> movingTrains;

    private List<Train> waitingTrains;

    private RailwaySavedData savedData;

    public GlobalRailwayManager() {
        this.cleanUp();
    }

    public void playerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.loadTrackData(serverPlayer.m_20194_());
            this.trackNetworks.values().forEach(g -> this.sync.sendFullGraphTo(g, serverPlayer));
            ArrayList<SignalEdgeGroup> asList = new ArrayList(this.signalEdgeGroups.values());
            this.sync.sendEdgeGroups(asList.stream().map(g -> g.id).toList(), asList.stream().map(g -> g.color).toList(), serverPlayer);
            for (Train train : this.trains.values()) {
                AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> serverPlayer), new TrainPacket(train, true));
            }
        }
    }

    public void playerLogout(Player player) {
    }

    public void levelLoaded(LevelAccessor level) {
        MinecraftServer server = level.getServer();
        if (server != null && server.overworld() == level) {
            this.cleanUp();
            this.savedData = null;
            this.loadTrackData(server);
        }
    }

    private void loadTrackData(MinecraftServer server) {
        if (this.savedData == null) {
            this.savedData = RailwaySavedData.load(server);
            this.trains = this.savedData.getTrains();
            this.trackNetworks = this.savedData.getTrackNetworks();
            this.signalEdgeGroups = this.savedData.getSignalBlocks();
            this.trains.values().forEach(this.movingTrains::add);
        }
    }

    public void cleanUp() {
        this.trackNetworks = new HashMap();
        this.signalEdgeGroups = new HashMap();
        this.trains = new HashMap();
        this.sync = new TrackGraphSync();
        this.movingTrains = new LinkedList();
        this.waitingTrains = new LinkedList();
        GlobalTrainDisplayData.statusByDestination.clear();
    }

    public void markTracksDirty() {
        if (this.savedData != null) {
            this.savedData.m_77762_();
        }
    }

    public void addTrain(Train train) {
        this.trains.put(train.id, train);
        this.movingTrains.add(train);
    }

    public void removeTrain(UUID id) {
        Train removed = (Train) this.trains.remove(id);
        if (removed != null) {
            this.movingTrains.remove(removed);
            this.waitingTrains.remove(removed);
        }
    }

    public TrackGraph getOrCreateGraph(UUID graphID, int netId) {
        return (TrackGraph) this.trackNetworks.computeIfAbsent(graphID, uid -> {
            TrackGraph trackGraph = new TrackGraph(graphID);
            trackGraph.setNetId(netId);
            return trackGraph;
        });
    }

    public void putGraphWithDefaultGroup(TrackGraph graph) {
        SignalEdgeGroup group = new SignalEdgeGroup(graph.id);
        this.signalEdgeGroups.put(graph.id, group.asFallback());
        this.sync.edgeGroupCreated(graph.id, group.color);
        this.putGraph(graph);
    }

    public void putGraph(TrackGraph graph) {
        this.trackNetworks.put(graph.id, graph);
        this.markTracksDirty();
    }

    public void removeGraphAndGroup(TrackGraph graph) {
        this.signalEdgeGroups.remove(graph.id);
        this.sync.edgeGroupRemoved(graph.id);
        this.removeGraph(graph);
    }

    public void removeGraph(TrackGraph graph) {
        this.trackNetworks.remove(graph.id);
        this.markTracksDirty();
    }

    public void updateSplitGraph(LevelAccessor level, TrackGraph graph) {
        Set<TrackGraph> disconnected = graph.findDisconnectedGraphs(level, null);
        disconnected.forEach(this::putGraphWithDefaultGroup);
        if (!disconnected.isEmpty()) {
            this.sync.graphSplit(graph, disconnected);
            this.markTracksDirty();
        }
    }

    @Nullable
    public TrackGraph getGraph(LevelAccessor level, TrackNodeLocation vertex) {
        if (this.trackNetworks == null) {
            return null;
        } else {
            for (TrackGraph railGraph : this.trackNetworks.values()) {
                if (railGraph.locateNode(vertex) != null) {
                    return railGraph;
                }
            }
            return null;
        }
    }

    public List<TrackGraph> getGraphs(LevelAccessor level, TrackNodeLocation vertex) {
        if (this.trackNetworks == null) {
            return Collections.emptyList();
        } else {
            ArrayList<TrackGraph> intersecting = new ArrayList();
            for (TrackGraph railGraph : this.trackNetworks.values()) {
                if (railGraph.locateNode(vertex) != null) {
                    intersecting.add(railGraph);
                }
            }
            return intersecting;
        }
    }

    public void tick(Level level) {
        if (level.dimension() == Level.OVERWORLD) {
            this.signalEdgeGroups.forEach((id, group) -> {
                group.trains.clear();
                group.reserved = null;
            });
            this.trackNetworks.forEach((id, graph) -> {
                graph.tickPoints(true);
                graph.resolveIntersectingEdgeGroups(level);
            });
            this.tickTrains(level);
            this.trackNetworks.forEach((id, graph) -> graph.tickPoints(false));
            GlobalTrainDisplayData.updateTick = level.getGameTime() % 100L == 0L;
            if (GlobalTrainDisplayData.updateTick) {
                GlobalTrainDisplayData.refresh();
            }
        }
    }

    private void tickTrains(Level level) {
        for (Train train : this.waitingTrains) {
            train.earlyTick(level);
        }
        for (Train train : this.movingTrains) {
            train.earlyTick(level);
        }
        for (Train train : this.waitingTrains) {
            train.tick(level);
        }
        for (Train train : this.movingTrains) {
            train.tick(level);
        }
        Iterator<Train> iterator = this.waitingTrains.iterator();
        while (iterator.hasNext()) {
            Train train = (Train) iterator.next();
            if (train.invalid) {
                iterator.remove();
                this.trains.remove(train.id);
                AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(train, false));
            } else if (train.navigation.waitingForSignal == null) {
                this.movingTrains.add(train);
                iterator.remove();
            }
        }
        iterator = this.movingTrains.iterator();
        while (iterator.hasNext()) {
            Train train = (Train) iterator.next();
            if (train.invalid) {
                iterator.remove();
                this.trains.remove(train.id);
                AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(train, false));
            } else if (train.navigation.waitingForSignal != null) {
                this.waitingTrains.add(train);
                iterator.remove();
            }
        }
    }

    public void tickSignalOverlay() {
        if (!isTrackGraphDebugActive()) {
            for (TrackGraph trackGraph : this.trackNetworks.values()) {
                TrackGraphVisualizer.visualiseSignalEdgeGroups(trackGraph);
            }
        }
    }

    public void clientTick() {
        if (isTrackGraphDebugActive()) {
            for (TrackGraph trackGraph : this.trackNetworks.values()) {
                TrackGraphVisualizer.debugViewGraph(trackGraph, isTrackGraphDebugExtended());
            }
        }
    }

    private static boolean isTrackGraphDebugActive() {
        return KineticDebugger.isF3DebugModeActive() && AllConfigs.client().showTrackGraphOnF3.get();
    }

    private static boolean isTrackGraphDebugExtended() {
        return AllConfigs.client().showExtendedTrackGraphOnF3.get();
    }

    public GlobalRailwayManager sided(LevelAccessor level) {
        if (level != null && !level.m_5776_()) {
            return this;
        } else {
            MutableObject<GlobalRailwayManager> m = new MutableObject();
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.clientManager(m));
            return (GlobalRailwayManager) m.getValue();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void clientManager(MutableObject<GlobalRailwayManager> m) {
        m.setValue(CreateClient.RAILWAYS);
    }
}