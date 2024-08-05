package com.mna.api.particles;

import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;

public interface IParticleMoveType {

    void serialize(FriendlyByteBuf var1);

    String serialize();

    IParticleMoveType deserialize(FriendlyByteBuf var1);

    void deserialize(String var1);

    void configureParticle(TextureSheetParticle var1);

    int getId();
}