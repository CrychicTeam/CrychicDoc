package com.simibubi.create.content.trains;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class TrainHUDUpdatePacket extends SimplePacketBase {

    UUID trainId;

    Double throttle;

    double speed;

    int fuelTicks;

    public TrainHUDUpdatePacket() {
    }

    public TrainHUDUpdatePacket(Train train) {
        this.trainId = train.id;
        this.throttle = train.throttle;
        this.speed = train.speedBeforeStall == null ? train.speed : train.speedBeforeStall;
        this.fuelTicks = train.fuelTicks;
    }

    public TrainHUDUpdatePacket(FriendlyByteBuf buffer) {
        this.trainId = buffer.readUUID();
        if (buffer.readBoolean()) {
            this.throttle = buffer.readDouble();
        }
        this.speed = buffer.readDouble();
        this.fuelTicks = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.trainId);
        buffer.writeBoolean(this.throttle != null);
        if (this.throttle != null) {
            buffer.writeDouble(this.throttle);
        }
        buffer.writeDouble(this.speed);
        buffer.writeInt(this.fuelTicks);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            boolean clientSide = sender == null;
            Train train = (Train) Create.RAILWAYS.sided(clientSide ? null : sender.m_9236_()).trains.get(this.trainId);
            if (train != null) {
                if (this.throttle != null) {
                    train.throttle = this.throttle;
                }
                if (clientSide) {
                    train.speed = this.speed;
                    train.fuelTicks = this.fuelTicks;
                }
            }
        });
        return true;
    }

    public static class Serverbound extends TrainHUDUpdatePacket {

        public Serverbound(FriendlyByteBuf buffer) {
            super(buffer);
        }

        public Serverbound(Train train, Double sendThrottle) {
            this.trainId = train.id;
            this.throttle = sendThrottle;
        }
    }
}