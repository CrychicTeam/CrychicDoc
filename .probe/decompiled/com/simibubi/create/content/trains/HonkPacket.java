package com.simibubi.create.content.trains;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class HonkPacket extends SimplePacketBase {

    UUID trainId;

    boolean isHonk;

    public HonkPacket() {
    }

    public HonkPacket(Train train, boolean isHonk) {
        this.trainId = train.id;
        this.isHonk = isHonk;
    }

    public HonkPacket(FriendlyByteBuf buffer) {
        this.trainId = buffer.readUUID();
        this.isHonk = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.trainId);
        buffer.writeBoolean(this.isHonk);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            boolean clientSide = sender == null;
            Train train = (Train) Create.RAILWAYS.sided(clientSide ? null : sender.m_9236_()).trains.get(this.trainId);
            if (train != null) {
                if (clientSide) {
                    if (this.isHonk) {
                        train.honkTicks = train.honkTicks == 0 ? 20 : 13;
                    } else {
                        train.honkTicks = train.honkTicks > 5 ? 6 : 0;
                    }
                } else {
                    AllAdvancements.TRAIN_WHISTLE.awardTo(sender);
                    AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new HonkPacket(train, this.isHonk));
                }
            }
        });
        return true;
    }

    public static class Serverbound extends HonkPacket {

        public Serverbound(FriendlyByteBuf buffer) {
            super(buffer);
        }

        public Serverbound(Train train, boolean isHonk) {
            super(train, isHonk);
        }
    }
}