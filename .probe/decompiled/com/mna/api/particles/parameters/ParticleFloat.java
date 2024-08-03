package com.mna.api.particles.parameters;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class ParticleFloat {

    private final float value;

    public ParticleFloat(float scale) {
        this.value = scale;
    }

    public float value() {
        return this.value;
    }

    public String serialize() {
        return this.value + "";
    }

    @Nullable
    public static ParticleFloat deserialize(String string) {
        return string != null && string != "" ? new ParticleFloat(Float.parseFloat(string)) : null;
    }

    @Nullable
    public static ParticleFloat deserialize(FriendlyByteBuf packetBuffer) {
        return packetBuffer.readBoolean() ? new ParticleFloat(packetBuffer.readFloat()) : null;
    }

    public static void serialize(@Nullable ParticleFloat inst, FriendlyByteBuf packetBuffer) {
        if (inst != null) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeFloat(inst.value());
        } else {
            packetBuffer.writeBoolean(false);
        }
    }
}