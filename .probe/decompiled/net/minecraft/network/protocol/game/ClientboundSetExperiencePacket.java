package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetExperiencePacket implements Packet<ClientGamePacketListener> {

    private final float experienceProgress;

    private final int totalExperience;

    private final int experienceLevel;

    public ClientboundSetExperiencePacket(float float0, int int1, int int2) {
        this.experienceProgress = float0;
        this.totalExperience = int1;
        this.experienceLevel = int2;
    }

    public ClientboundSetExperiencePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.experienceProgress = friendlyByteBuf0.readFloat();
        this.experienceLevel = friendlyByteBuf0.readVarInt();
        this.totalExperience = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeFloat(this.experienceProgress);
        friendlyByteBuf0.writeVarInt(this.experienceLevel);
        friendlyByteBuf0.writeVarInt(this.totalExperience);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetExperience(this);
    }

    public float getExperienceProgress() {
        return this.experienceProgress;
    }

    public int getTotalExperience() {
        return this.totalExperience;
    }

    public int getExperienceLevel() {
        return this.experienceLevel;
    }
}