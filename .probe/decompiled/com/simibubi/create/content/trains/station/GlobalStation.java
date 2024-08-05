package com.simibubi.create.content.trains.station;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlobalStation extends SingleBlockEntityEdgePoint {

    public String name = "Track Station";

    public WeakReference<Train> nearestTrain = new WeakReference(null);

    public boolean assembling;

    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front) {
        super.blockEntityAdded(blockEntity, front);
        BlockState state = blockEntity.getBlockState();
        this.assembling = state != null && state.m_61138_(StationBlock.ASSEMBLING) && (Boolean) state.m_61143_(StationBlock.ASSEMBLING);
    }

    @Override
    public void read(CompoundTag nbt, boolean migration, DimensionPalette dimensions) {
        super.read(nbt, migration, dimensions);
        this.name = nbt.getString("Name");
        this.assembling = nbt.getBoolean("Assembling");
        this.nearestTrain = new WeakReference(null);
    }

    @Override
    public void read(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        super.read(buffer, dimensions);
        this.name = buffer.readUtf();
        this.assembling = buffer.readBoolean();
        if (buffer.readBoolean()) {
            this.blockEntityPos = buffer.readBlockPos();
        }
    }

    @Override
    public void write(CompoundTag nbt, DimensionPalette dimensions) {
        super.write(nbt, dimensions);
        nbt.putString("Name", this.name);
        nbt.putBoolean("Assembling", this.assembling);
    }

    @Override
    public void write(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        super.write(buffer, dimensions);
        buffer.writeUtf(this.name);
        buffer.writeBoolean(this.assembling);
        buffer.writeBoolean(this.blockEntityPos != null);
        if (this.blockEntityPos != null) {
            buffer.writeBlockPos(this.blockEntityPos);
        }
    }

    public boolean canApproachFrom(TrackNode side) {
        return this.isPrimary(side) && !this.assembling;
    }

    @Override
    public boolean canNavigateVia(TrackNode side) {
        return super.canNavigateVia(side) && !this.assembling;
    }

    public void reserveFor(Train train) {
        Train nearestTrain = this.getNearestTrain();
        if (nearestTrain == null || nearestTrain.navigation.distanceToDestination > train.navigation.distanceToDestination) {
            this.nearestTrain = new WeakReference(train);
        }
    }

    public void cancelReservation(Train train) {
        if (this.nearestTrain.get() == train) {
            this.nearestTrain = new WeakReference(null);
        }
    }

    public void trainDeparted(Train train) {
        this.cancelReservation(train);
    }

    @Nullable
    public Train getPresentTrain() {
        Train nearestTrain = this.getNearestTrain();
        return nearestTrain != null && nearestTrain.getCurrentStation() == this ? nearestTrain : null;
    }

    @Nullable
    public Train getImminentTrain() {
        Train nearestTrain = this.getNearestTrain();
        if (nearestTrain == null) {
            return nearestTrain;
        } else if (nearestTrain.getCurrentStation() == this) {
            return nearestTrain;
        } else if (!nearestTrain.navigation.isActive()) {
            return null;
        } else {
            return nearestTrain.navigation.distanceToDestination > 30.0 ? null : nearestTrain;
        }
    }

    @Nullable
    public Train getNearestTrain() {
        return (Train) this.nearestTrain.get();
    }
}