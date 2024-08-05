package com.almostreliable.summoningrituals.network;

import com.almostreliable.summoningrituals.altar.AltarBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ClientAltarUpdatePacket extends ServerToClientPacket<ClientAltarUpdatePacket> {

    private BlockPos pos;

    private ClientAltarUpdatePacket.PacketType type;

    private int value;

    private ClientAltarUpdatePacket(BlockPos pos, ClientAltarUpdatePacket.PacketType type, int value) {
        this.pos = pos;
        this.type = type;
        this.value = value;
    }

    ClientAltarUpdatePacket() {
    }

    public static ClientAltarUpdatePacket progressUpdate(BlockPos pos, int progress) {
        return new ClientAltarUpdatePacket(pos, ClientAltarUpdatePacket.PacketType.PROGRESS, progress);
    }

    public static ClientAltarUpdatePacket processTimeUpdate(BlockPos pos, int processTime) {
        return new ClientAltarUpdatePacket(pos, ClientAltarUpdatePacket.PacketType.PROCESS_TIME, processTime);
    }

    public void encode(ClientAltarUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeEnum(packet.type);
        buffer.writeInt(packet.value);
    }

    public ClientAltarUpdatePacket decode(FriendlyByteBuf buffer) {
        return new ClientAltarUpdatePacket(buffer.readBlockPos(), buffer.readEnum(ClientAltarUpdatePacket.PacketType.class), buffer.readInt());
    }

    protected void handlePacket(ClientAltarUpdatePacket packet, ClientLevel level) {
        if (level.m_7702_(packet.pos) instanceof AltarBlockEntity altar) {
            if (packet.type == ClientAltarUpdatePacket.PacketType.PROGRESS) {
                altar.setProgress(packet.value);
            } else {
                if (packet.type != ClientAltarUpdatePacket.PacketType.PROCESS_TIME) {
                    throw new IllegalStateException("Unknown packet type: " + packet.type);
                }
                altar.setProcessTime(packet.value);
            }
        }
    }

    public static enum PacketType {

        PROGRESS, PROCESS_TIME
    }
}