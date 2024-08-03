package com.simibubi.create.content.equipment.bell;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BasicParticleData<T extends Particle> implements ParticleOptions, ICustomParticleDataWithSprite<BasicParticleData<T>> {

    @Override
    public ParticleOptions.Deserializer<BasicParticleData<T>> getDeserializer() {
        final BasicParticleData<T> data = this;
        return new ParticleOptions.Deserializer<BasicParticleData<T>>() {

            public BasicParticleData<T> fromCommand(ParticleType<BasicParticleData<T>> arg0, StringReader reader) {
                return data;
            }

            public BasicParticleData<T> fromNetwork(ParticleType<BasicParticleData<T>> type, FriendlyByteBuf buffer) {
                return data;
            }
        };
    }

    @Override
    public Codec<BasicParticleData<T>> getCodec(ParticleType<BasicParticleData<T>> type) {
        return Codec.unit(this);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract BasicParticleData.IBasicParticleFactory<T> getBasicFactory();

    @OnlyIn(Dist.CLIENT)
    @Override
    public ParticleEngine.SpriteParticleRegistration<BasicParticleData<T>> getMetaFactory() {
        return animatedSprite -> (data, worldIn, x, y, z, vx, vy, vz) -> this.getBasicFactory().makeParticle(worldIn, x, y, z, vx, vy, vz, animatedSprite);
    }

    @Override
    public String writeToString() {
        return RegisteredObjects.getKeyOrThrow(this.m_6012_()).toString();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    public interface IBasicParticleFactory<U extends Particle> {

        U makeParticle(ClientLevel var1, double var2, double var4, double var6, double var8, double var10, double var12, SpriteSet var14);
    }
}