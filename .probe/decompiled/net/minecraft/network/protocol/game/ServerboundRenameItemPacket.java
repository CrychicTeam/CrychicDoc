package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundRenameItemPacket implements Packet<ServerGamePacketListener> {

    private final String name;

    public ServerboundRenameItemPacket(String string0) {
        this.name = string0;
    }

    public ServerboundRenameItemPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.name = friendlyByteBuf0.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.name);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleRenameItem(this);
    }

    public String getName() {
        return this.name;
    }
}