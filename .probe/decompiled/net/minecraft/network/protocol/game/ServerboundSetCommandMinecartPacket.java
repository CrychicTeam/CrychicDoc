package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;

public class ServerboundSetCommandMinecartPacket implements Packet<ServerGamePacketListener> {

    private final int entity;

    private final String command;

    private final boolean trackOutput;

    public ServerboundSetCommandMinecartPacket(int int0, String string1, boolean boolean2) {
        this.entity = int0;
        this.command = string1;
        this.trackOutput = boolean2;
    }

    public ServerboundSetCommandMinecartPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entity = friendlyByteBuf0.readVarInt();
        this.command = friendlyByteBuf0.readUtf();
        this.trackOutput = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entity);
        friendlyByteBuf0.writeUtf(this.command);
        friendlyByteBuf0.writeBoolean(this.trackOutput);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetCommandMinecart(this);
    }

    @Nullable
    public BaseCommandBlock getCommandBlock(Level level0) {
        Entity $$1 = level0.getEntity(this.entity);
        return $$1 instanceof MinecartCommandBlock ? ((MinecartCommandBlock) $$1).getCommandBlock() : null;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isTrackOutput() {
        return this.trackOutput;
    }
}