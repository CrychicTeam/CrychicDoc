package com.rekindled.embers.network.message;

import com.rekindled.embers.api.tile.IDialEntity;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class MessageDialUpdateRequest {

    public long pos = 0L;

    public int maxLines;

    public MessageDialUpdateRequest(long pos, int maxLines) {
        this.pos = pos;
        this.maxLines = maxLines;
    }

    public MessageDialUpdateRequest(BlockPos pos, int maxLines) {
        this.pos = pos.asLong();
        this.maxLines = maxLines;
    }

    public static void encode(MessageDialUpdateRequest msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.pos);
        buf.writeInt(msg.maxLines);
    }

    public static MessageDialUpdateRequest decode(FriendlyByteBuf buf) {
        return new MessageDialUpdateRequest(buf.readLong(), buf.readInt());
    }

    public static void handle(MessageDialUpdateRequest msg, Supplier<NetworkEvent.Context> ctx) {
        if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isServer()) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                ServerPlayer player = ((NetworkEvent.Context) ctx.get()).getSender();
                if (player != null && player.m_9236_() != null && player.m_9236_().getBlockEntity(BlockPos.of(msg.pos)) instanceof IDialEntity dial) {
                    Packet<ClientGamePacketListener> packet = dial.getUpdatePacket(msg.maxLines);
                    if (packet != null) {
                        player.connection.send(packet);
                    }
                }
            });
        }
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}