package com.mna.api.particles.parameters;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class ParticleInt {

    private final int value;

    public ParticleInt(int life) {
        this.value = life;
    }

    public int value() {
        return this.value;
    }

    public String serialize() {
        return this.value + "";
    }

    @Nullable
    public static ParticleInt deserialize(String string) {
        return string != null && string != "" ? new ParticleInt(Integer.parseInt(string)) : null;
    }

    @Nullable
    public static ParticleInt deserialize(FriendlyByteBuf packetBuffer) {
        return packetBuffer.readBoolean() ? new ParticleInt(packetBuffer.readInt()) : null;
    }

    public static void serialize(@Nullable ParticleInt inst, FriendlyByteBuf packetBuffer) {
        if (inst != null) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeInt(inst.value());
        } else {
            packetBuffer.writeBoolean(false);
        }
    }
}