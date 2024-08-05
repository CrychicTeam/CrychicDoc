package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.ExperienceOrb;

public class ClientboundAddExperienceOrbPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    private final double x;

    private final double y;

    private final double z;

    private final int value;

    public ClientboundAddExperienceOrbPacket(ExperienceOrb experienceOrb0) {
        this.id = experienceOrb0.m_19879_();
        this.x = experienceOrb0.m_20185_();
        this.y = experienceOrb0.m_20186_();
        this.z = experienceOrb0.m_20189_();
        this.value = experienceOrb0.getValue();
    }

    public ClientboundAddExperienceOrbPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.value = friendlyByteBuf0.readShort();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeShort(this.value);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAddExperienceOrb(this);
    }

    public int getId() {
        return this.id;
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

    public int getValue() {
        return this.value;
    }
}