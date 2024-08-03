package com.simibubi.create.content.trains.signal;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TrackEdgePoint {

    public UUID id;

    public Couple<TrackNodeLocation> edgeLocation;

    public double position;

    private EdgePointType<?> type;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

    public void setType(EdgePointType<?> type) {
        this.type = type;
    }

    public EdgePointType<?> getType() {
        return this.type;
    }

    public abstract boolean canMerge();

    public boolean canCoexistWith(EdgePointType<?> otherType, boolean front) {
        return false;
    }

    public abstract void invalidate(LevelAccessor var1);

    protected void invalidateAt(LevelAccessor level, BlockPos blockEntityPos) {
        TrackTargetingBehaviour<?> behaviour = BlockEntityBehaviour.get(level, blockEntityPos, TrackTargetingBehaviour.TYPE);
        if (behaviour != null) {
            CompoundTag migrationData = new CompoundTag();
            DimensionPalette dimensions = new DimensionPalette();
            this.write(migrationData, dimensions);
            dimensions.write(migrationData);
            behaviour.invalidateEdgePoint(migrationData);
        }
    }

    public abstract void blockEntityAdded(BlockEntity var1, boolean var2);

    public abstract void blockEntityRemoved(BlockPos var1, boolean var2);

    public void onRemoved(TrackGraph graph) {
    }

    public void setLocation(Couple<TrackNodeLocation> nodes, double position) {
        this.edgeLocation = nodes;
        this.position = position;
    }

    public double getLocationOn(TrackEdge edge) {
        return this.isPrimary(edge.node1) ? edge.getLength() - this.position : this.position;
    }

    public boolean canNavigateVia(TrackNode side) {
        return true;
    }

    public boolean isPrimary(TrackNode node1) {
        return this.edgeLocation.getSecond().equals(node1.getLocation());
    }

    public void read(CompoundTag nbt, boolean migration, DimensionPalette dimensions) {
        if (!migration) {
            this.id = nbt.getUUID("Id");
            this.position = nbt.getDouble("Position");
            this.edgeLocation = Couple.deserializeEach(nbt.getList("Edge", 10), tag -> TrackNodeLocation.read(tag, dimensions));
        }
    }

    public void read(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        this.id = buffer.readUUID();
        this.edgeLocation = Couple.create(() -> TrackNodeLocation.receive(buffer, dimensions));
        this.position = buffer.readDouble();
    }

    public void write(CompoundTag nbt, DimensionPalette dimensions) {
        nbt.putUUID("Id", this.id);
        nbt.putDouble("Position", this.position);
        nbt.put("Edge", this.edgeLocation.serializeEach(loc -> loc.write(dimensions)));
    }

    public void write(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        buffer.writeResourceLocation(this.type.getId());
        buffer.writeUUID(this.id);
        this.edgeLocation.forEach(loc -> loc.send(buffer, dimensions));
        buffer.writeDouble(this.position);
    }

    public void tick(TrackGraph graph, boolean preTrains) {
    }

    protected void removeFromAllGraphs() {
        for (TrackGraph trackGraph : Create.RAILWAYS.trackNetworks.values()) {
            if (trackGraph.removePoint(this.getType(), this.id) != null) {
                return;
            }
        }
    }
}