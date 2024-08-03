package com.simibubi.create.content.trains.entity;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class TrainPacket extends SimplePacketBase {

    UUID trainId;

    Train train;

    boolean add;

    public TrainPacket(Train train, boolean add) {
        this.train = train;
        this.add = add;
    }

    public TrainPacket(FriendlyByteBuf buffer) {
        this.add = buffer.readBoolean();
        this.trainId = buffer.readUUID();
        if (this.add) {
            UUID owner = null;
            if (buffer.readBoolean()) {
                owner = buffer.readUUID();
            }
            List<Carriage> carriages = new ArrayList();
            List<Integer> carriageSpacing = new ArrayList();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                Couple<CarriageBogey> bogies = Couple.create(null, null);
                for (boolean isFirst : Iterate.trueAndFalse) {
                    if (isFirst || buffer.readBoolean()) {
                        AbstractBogeyBlock<?> type = (AbstractBogeyBlock<?>) ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
                        boolean upsideDown = buffer.readBoolean();
                        CompoundTag data = buffer.readNbt();
                        bogies.set(isFirst, new CarriageBogey(type, upsideDown, data, new TravellingPoint(), new TravellingPoint()));
                    }
                }
                int spacing = buffer.readVarInt();
                carriages.add(new Carriage(bogies.getFirst(), bogies.getSecond(), spacing));
            }
            size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                carriageSpacing.add(buffer.readVarInt());
            }
            boolean doubleEnded = buffer.readBoolean();
            this.train = new Train(this.trainId, owner, null, carriages, carriageSpacing, doubleEnded);
            this.train.name = Component.Serializer.fromJson(buffer.readUtf());
            this.train.icon = TrainIconType.byId(buffer.readResourceLocation());
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.add);
        buffer.writeUUID(this.train.id);
        if (this.add) {
            buffer.writeBoolean(this.train.owner != null);
            if (this.train.owner != null) {
                buffer.writeUUID(this.train.owner);
            }
            buffer.writeVarInt(this.train.carriages.size());
            for (Carriage carriage : this.train.carriages) {
                for (boolean first : Iterate.trueAndFalse) {
                    if (!first) {
                        boolean onTwoBogeys = carriage.isOnTwoBogeys();
                        buffer.writeBoolean(onTwoBogeys);
                        if (!onTwoBogeys) {
                            continue;
                        }
                    }
                    CarriageBogey bogey = carriage.bogeys.get(first);
                    buffer.writeResourceLocation(RegisteredObjects.getKeyOrThrow(bogey.type));
                    buffer.writeBoolean(bogey.upsideDown);
                    buffer.writeNbt(bogey.bogeyData);
                }
                buffer.writeVarInt(carriage.bogeySpacing);
            }
            buffer.writeVarInt(this.train.carriageSpacing.size());
            this.train.carriageSpacing.forEach(buffer::m_130130_);
            buffer.writeBoolean(this.train.doubleEnded);
            buffer.writeUtf(Component.Serializer.toJson(this.train.name));
            buffer.writeResourceLocation(this.train.icon.id);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Map<UUID, Train> trains = CreateClient.RAILWAYS.trains;
            if (this.add) {
                trains.put(this.train.id, this.train);
            } else {
                trains.remove(this.trainId);
            }
        });
        return true;
    }
}