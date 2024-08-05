package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientboundSetCameraPacket implements Packet<ClientGamePacketListener> {

    private final int cameraId;

    public ClientboundSetCameraPacket(Entity entity0) {
        this.cameraId = entity0.getId();
    }

    public ClientboundSetCameraPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.cameraId = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.cameraId);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetCamera(this);
    }

    @Nullable
    public Entity getEntity(Level level0) {
        return level0.getEntity(this.cameraId);
    }
}