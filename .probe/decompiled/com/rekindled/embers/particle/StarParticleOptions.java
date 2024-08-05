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

public class StarParticleOptions implements ParticleOptions {

    public static final float MIN_SCALE = 0.01F;

    public static final float MAX_SCALE = 4.0F;

    protected final Vector3f color;

    protected final float scale;

    public static final StarParticleOptions EMBER = new StarParticleOptions(GlowParticleOptions.EMBER_COLOR, 2.0F);

    public static final Codec<StarParticleOptions> CODEC = RecordCodecBuilder.create(p_175793_ -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_175797_ -> p_175797_.color), Codec.FLOAT.fieldOf("scale").forGetter(p_175795_ -> p_175795_.scale)).apply(p_175793_, StarParticleOptions::new));

    public static final ParticleOptions.Deserializer<StarParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<StarParticleOptions>() {

        public StarParticleOptions fromCommand(ParticleType<StarParticleOptions> p_123689_, StringReader p_123690_) throws CommandSyntaxException {
            Vector3f vector3f = StarParticleOptions.readVector3f(p_123690_);
            p_123690_.expect(' ');
            float f = p_123690_.readFloat();
            return new StarParticleOptions(vector3f, f);
        }

        public StarParticleOptions fromNetwork(ParticleType<StarParticleOptions> p_123692_, FriendlyByteBuf p_123693_) {
            return new StarParticleOptions(StarParticleOptions.readVector3f(p_123693_), p_123693_.readFloat());
        }
    };

    public StarParticleOptions(Vector3f pColor, float pScale) {
        this.color = pColor;
        this.scale = pScale;
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
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.scale);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryManager.STAR_PARTICLE.get();
    }
}