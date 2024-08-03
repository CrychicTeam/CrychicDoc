package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class ServerboundPlayerCommandPacket implements Packet<ServerGamePacketListener> {

    private final int id;

    private final ServerboundPlayerCommandPacket.Action action;

    private final int data;

    public ServerboundPlayerCommandPacket(Entity entity0, ServerboundPlayerCommandPacket.Action serverboundPlayerCommandPacketAction1) {
        this(entity0, serverboundPlayerCommandPacketAction1, 0);
    }

    public ServerboundPlayerCommandPacket(Entity entity0, ServerboundPlayerCommandPacket.Action serverboundPlayerCommandPacketAction1, int int2) {
        this.id = entity0.getId();
        this.action = serverboundPlayerCommandPacketAction1;
        this.data = int2;
    }

    public ServerboundPlayerCommandPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.action = friendlyByteBuf0.readEnum(ServerboundPlayerCommandPacket.Action.class);
        this.data = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeEnum(this.action);
        friendlyByteBuf0.writeVarInt(this.data);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePlayerCommand(this);
    }

    public int getId() {
        return this.id;
    }

    public ServerboundPlayerCommandPacket.Action getAction() {
        return this.action;
    }

    public int getData() {
        return this.data;
    }

    public static enum Action {

        PRESS_SHIFT_KEY,
        RELEASE_SHIFT_KEY,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_INVENTORY,
        START_FALL_FLYING
    }
}