package com.mna.api.particles.parameters;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class ParticleBoolean {

    private final boolean value;

    public ParticleBoolean(boolean life) {
        this.value = life;
    }

    public boolean value() {
        return this.value;
    }

    public String serialize() {
        return this.value + "";
    }

    public static ParticleBoolean deserialize(String string) {
        return new ParticleBoolean(Boolean.parseBoolean(string));
    }

    @Nullable
    public static ParticleBoolean deserialize(FriendlyByteBuf packetBuffer) {
        return new ParticleBoolean(packetBuffer.readBoolean());
    }

    public static void serialize(@Nullable ParticleBoolean inst, FriendlyByteBuf packetBuffer) {
        if (inst != null) {
            packetBuffer.writeBoolean(inst.value());
        } else {
            packetBuffer.writeBoolean(false);
        }
    }
}