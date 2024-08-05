package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;

public class ClientboundAddPlayerPacket implements Packet<ClientGamePacketListener> {

    private final int entityId;

    private final UUID playerId;

    private final double x;

    private final double y;

    private final double z;

    private final byte yRot;

    private final byte xRot;

    public ClientboundAddPlayerPacket(Player player0) {
        this.entityId = player0.m_19879_();
        this.playerId = player0.getGameProfile().getId();
        this.x = player0.m_20185_();
        this.y = player0.m_20186_();
        this.z = player0.m_20189_();
        this.yRot = (byte) ((int) (player0.m_146908_() * 256.0F / 360.0F));
        this.xRot = (byte) ((int) (player0.m_146909_() * 256.0F / 360.0F));
    }

    public ClientboundAddPlayerPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        this.playerId = friendlyByteBuf0.readUUID();
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.yRot = friendlyByteBuf0.readByte();
        this.xRot = friendlyByteBuf0.readByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeUUID(this.playerId);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeByte(this.yRot);
        friendlyByteBuf0.writeByte(this.xRot);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAddPlayer(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public byte getyRot() {
        return this.yRot;
    }

    public byte getxRot() {
        return this.xRot;
    }
}