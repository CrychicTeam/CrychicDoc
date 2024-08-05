package io.redspace.ironsspellbooks.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class BlastwaveParticleOptions extends DustParticleOptionsBase {

    private float scale;

    public static final Codec<BlastwaveParticleOptions> CODEC = RecordCodecBuilder.create(p_175793_ -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_175797_ -> p_175797_.f_175800_), Codec.FLOAT.fieldOf("scale").forGetter(p_175795_ -> p_175795_.scale)).apply(p_175793_, BlastwaveParticleOptions::new));

    public static final ParticleOptions.Deserializer<BlastwaveParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<BlastwaveParticleOptions>() {

        @NotNull
        public BlastwaveParticleOptions fromCommand(@NotNull ParticleType<BlastwaveParticleOptions> p_123689_, @NotNull StringReader p_123690_) throws CommandSyntaxException {
            Vector3f vector3f = DustParticleOptionsBase.readVector3f(p_123690_);
            p_123690_.expect(' ');
            float f = p_123690_.readFloat();
            return new BlastwaveParticleOptions(vector3f, f);
        }

        @NotNull
        public BlastwaveParticleOptions fromNetwork(@NotNull ParticleType<BlastwaveParticleOptions> p_123692_, @NotNull FriendlyByteBuf p_123693_) {
            return new BlastwaveParticleOptions(DustParticleOptionsBase.readVector3f(p_123693_), p_123693_.readFloat());
        }
    };

    public BlastwaveParticleOptions(Vector3f color, float scale) {
        super(color, scale);
        this.scale = scale;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.f_175800_.x());
        pBuffer.writeFloat(this.f_175800_.y());
        pBuffer.writeFloat(this.f_175800_.z());
        pBuffer.writeFloat(this.scale);
    }

    @NotNull
    @Override
    public ParticleType<BlastwaveParticleOptions> getType() {
        return ParticleRegistry.BLASTWAVE_PARTICLE.get();
    }
}