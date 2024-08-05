package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ContraptionColliderLockPacket extends SimplePacketBase {

    protected int contraption;

    protected double offset;

    private int sender;

    public ContraptionColliderLockPacket(int contraption, double offset, int sender) {
        this.contraption = contraption;
        this.offset = offset;
        this.sender = sender;
    }

    public ContraptionColliderLockPacket(FriendlyByteBuf buffer) {
        this.contraption = buffer.readVarInt();
        this.offset = buffer.readDouble();
        this.sender = buffer.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.contraption);
        buffer.writeDouble(this.offset);
        buffer.writeVarInt(this.sender);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ContraptionCollider.lockPacketReceived(this.contraption, this.sender, this.offset));
        return true;
    }

    public static class ContraptionColliderLockPacketRequest extends SimplePacketBase {

        protected int contraption;

        protected double offset;

        public ContraptionColliderLockPacketRequest(int contraption, double offset) {
            this.contraption = contraption;
            this.offset = offset;
        }

        public ContraptionColliderLockPacketRequest(FriendlyByteBuf buffer) {
            this.contraption = buffer.readVarInt();
            this.offset = buffer.readDouble();
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeVarInt(this.contraption);
            buffer.writeDouble(this.offset);
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(context::getSender), new ContraptionColliderLockPacket(this.contraption, this.offset, context.getSender().m_19879_())));
            return true;
        }
    }
}