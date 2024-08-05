package com.rekindled.embers.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rekindled.embers.RegistryManager;
import java.util.Locale;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class AlchemyCircleParticleOptions implements ParticleOptions {

    protected final Vector3f color;

    protected final float scale;

    protected final int lifetime;

    public static final AlchemyCircleParticleOptions DEFAULT = new AlchemyCircleParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0F, 420);

    public static final Codec<AlchemyCircleParticleOptions> CODEC = RecordCodecBuilder.create(p_175793_ -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_175797_ -> p_175797_.color), Codec.FLOAT.fieldOf("scale").forGetter(p_175795_ -> p_175795_.scale), Codec.INT.fieldOf("lifetime").forGetter(p_175795_ -> p_175795_.lifetime)).apply(p_175793_, AlchemyCircleParticleOptions::new));

    public static final ParticleOptions.Deserializer<AlchemyCircleParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<AlchemyCircleParticleOptions>() {

        public AlchemyCircleParticleOptions fromCommand(ParticleType<AlchemyCircleParticleOptions> p_123689_, StringReader p_123690_) throws CommandSyntaxException {
            Vector3f vector3f = AlchemyCircleParticleOptions.readVector3f(p_123690_);
            p_123690_.expect(' ');
            float f = p_123690_.readFloat();
            int i = p_123690_.readInt();
            return new AlchemyCircleParticleOptions(vector3f, f, i);
        }

        public AlchemyCircleParticleOptions fromNetwork(ParticleType<AlchemyCircleParticleOptions> p_123692_, FriendlyByteBuf p_123693_) {
            return new AlchemyCircleParticleOptions(AlchemyCircleParticleOptions.readVector3f(p_123693_), p_123693_.readFloat(), p_123693_.readInt());
        }
    };

    public AlchemyCircleParticleOptions(Vector3f pColor, float pScale, int lifetime) {
        this.color = pColor;
        this.scale = pScale;
        this.lifetime = lifetime;
    }

    public static Vector3f readVector3f(StringReader pStringInput) throws CommandSyntaxException {
        pStringInput.expect(' ');
        float f = pStringInput.readFloat();
        pStringInput.expect(' ');
        float f1 = pStringInput.readFloat();
        pStringInput.expect(' ');
        float f2 = pStringInput.readFloat();
        return new Vector3f(f, f1, f2);
    }

    public static Vector3f readVector3f(FriendlyByteBuf pBuffer) {
        return new Vector3f(pBuffer.readFloat(), pBuffer.readFloat(), pBuffer.readFloat());
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.color.x());
        pBuffer.writeFloat(this.color.y());
        pBuffer.writeFloat(this.color.z());
        pBuffer.writeFloat(this.scale);
        pBuffer.writeInt(this.lifetime);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.scale, this.lifetime);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryManager.ALCHEMY_CIRCLE_PARTICLE.get();
    }
}