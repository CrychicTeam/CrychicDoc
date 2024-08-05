package net.minecraft.network.protocol.game;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class ServerboundTeleportToEntityPacket implements Packet<ServerGamePacketListener> {

    private final UUID uuid;

    public ServerboundTeleportToEntityPacket(UUID uUID0) {
        this.uuid = uUID0;
    }

    public ServerboundTeleportToEntityPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.uuid = friendlyByteBuf0.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUUID(this.uuid);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleTeleportToEntityPacket(this);
    }

    @Nullable
    public Entity getEntity(ServerLevel serverLevel0) {
        return serverLevel0.getEntity(this.uuid);
    }
}