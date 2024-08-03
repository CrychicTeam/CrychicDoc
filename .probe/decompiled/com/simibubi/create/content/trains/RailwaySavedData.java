package com.simibubi.create.content.trains;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class RailwaySavedData extends SavedData {

    private Map<UUID, TrackGraph> trackNetworks = new HashMap();

    private Map<UUID, SignalEdgeGroup> signalEdgeGroups = new HashMap();

    private Map<UUID, Train> trains = new HashMap();

    @Override
    public CompoundTag save(CompoundTag nbt) {
        GlobalRailwayManager railways = Create.RAILWAYS;
        DimensionPalette dimensions = new DimensionPalette();
        nbt.put("RailGraphs", NBTHelper.writeCompoundList(railways.trackNetworks.values(), tg -> tg.write(dimensions)));
        nbt.put("SignalBlocks", NBTHelper.writeCompoundList(railways.signalEdgeGroups.values(), seg -> seg.fallbackGroup && !railways.trackNetworks.containsKey(seg.id) ? null : seg.write()));
        nbt.put("Trains", NBTHelper.writeCompoundList(railways.trains.values(), t -> t.write(dimensions)));
        dimensions.write(nbt);
        return nbt;
    }

    private static RailwaySavedData load(CompoundTag nbt) {
        RailwaySavedData sd = new RailwaySavedData();
        sd.trackNetworks = new HashMap();
        sd.signalEdgeGroups = new HashMap();
        sd.trains = new HashMap();
        DimensionPalette dimensions = DimensionPalette.read(nbt);
        NBTHelper.iterateCompoundList(nbt.getList("RailGraphs", 10), c -> {
            TrackGraph graphx = TrackGraph.read(c, dimensions);
            sd.trackNetworks.put(graphx.id, graphx);
        });
        NBTHelper.iterateCompoundList(nbt.getList("SignalBlocks", 10), c -> {
            SignalEdgeGroup groupx = SignalEdgeGroup.read(c);
            sd.signalEdgeGroups.put(groupx.id, groupx);
        });
        NBTHelper.iterateCompoundList(nbt.getList("Trains", 10), c -> {
            Train train = Train.read(c, sd.trackNetworks, dimensions);
            sd.trains.put(train.id, train);
        });
        for (TrackGraph graph : sd.trackNetworks.values()) {
            for (SignalBoundary signal : graph.getPoints(EdgePointType.SIGNAL)) {
                UUID groupId = signal.groups.getFirst();
                UUID otherGroupId = signal.groups.getSecond();
                if (groupId != null && otherGroupId != null) {
                    SignalEdgeGroup group = (SignalEdgeGroup) sd.signalEdgeGroups.get(groupId);
                    SignalEdgeGroup otherGroup = (SignalEdgeGroup) sd.signalEdgeGroups.get(otherGroupId);
                    if (group != null && otherGroup != null) {
                        group.putAdjacent(otherGroupId);
                        otherGroup.putAdjacent(groupId);
                    }
                }
            }
        }
        return sd;
    }

    public Map<UUID, TrackGraph> getTrackNetworks() {
        return this.trackNetworks;
    }

    public Map<UUID, Train> getTrains() {
        return this.trains;
    }

    public Map<UUID, SignalEdgeGroup> getSignalBlocks() {
        return this.signalEdgeGroups;
    }

    private RailwaySavedData() {
    }

    public static RailwaySavedData load(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(RailwaySavedData::load, RailwaySavedData::new, "create_tracks");
    }
}