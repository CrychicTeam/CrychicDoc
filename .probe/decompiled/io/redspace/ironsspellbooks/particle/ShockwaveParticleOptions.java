package io.redspace.ironsspellbooks.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import java.util.Optional;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ShockwaveParticleOptions extends DustParticleOptionsBase {

    protected final float unclampedScale;

    protected final boolean fullbright;

    protected final String trailParticleRaw;

    public static final Codec<ShockwaveParticleOptions> CODEC = RecordCodecBuilder.create(p_175793_ -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(option -> option.f_175800_), Codec.FLOAT.fieldOf("scale").forGetter(option -> option.unclampedScale), Codec.BOOL.fieldOf("fullbright").forGetter(option -> option.fullbright), Codec.STRING.fieldOf("particle").forGetter(option -> option.trailParticleRaw)).apply(p_175793_, ShockwaveParticleOptions::new));

    public static final ParticleOptions.Deserializer<ShockwaveParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<ShockwaveParticleOptions>() {

        @NotNull
        public ShockwaveParticleOptions fromCommand(@NotNull ParticleType<ShockwaveParticleOptions> p_123689_, @NotNull StringReader reader) throws CommandSyntaxException {
            Vector3f vector3f = DustParticleOptionsBase.readVector3f(reader);
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            boolean glowing = reader.readBoolean();
            reader.expect(' ');
            String particle = reader.readString();
            return new ShockwaveParticleOptions(vector3f, f, glowing, particle);
        }

        @NotNull
        public ShockwaveParticleOptions fromNetwork(@NotNull ParticleType<ShockwaveParticleOptions> p_123692_, @NotNull FriendlyByteBuf buf) {
            return new ShockwaveParticleOptions(DustParticleOptionsBase.readVector3f(buf), buf.readFloat(), buf.readBoolean(), buf.readUtf());
        }
    };

    public ShockwaveParticleOptions(Vector3f color, float scale, boolean glowing, String trailParticle) {
        super(color, scale);
        this.unclampedScale = scale;
        this.fullbright = glowing;
        this.trailParticleRaw = trailParticle;
    }

    public ShockwaveParticleOptions(Vector3f color, float scale, boolean glowing) {
        this(color, scale, glowing, "");
    }

    @Override
    public float getScale() {
        return this.unclampedScale;
    }

    public boolean isFullbright() {
        return this.fullbright;
    }

    public Optional<ParticleOptions> trailParticle() {
        try {
            ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(this.trailParticleRaw));
            if (type instanceof ParticleOptions particleOptions) {
                return Optional.of(particleOptions);
            }
        } catch (Exception var3) {
        }
        return Optional.empty();
    }

    public Vector3f color() {
        return this.f_175800_;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.f_175800_.x());
        pBuffer.writeFloat(this.f_175800_.y());
        pBuffer.writeFloat(this.f_175800_.z());
        pBuffer.writeFloat(this.unclampedScale);
        pBuffer.writeBoolean(this.fullbright);
        pBuffer.writeUtf(this.trailParticleRaw);
    }

    @NotNull
    @Override
    public ParticleType<ShockwaveParticleOptions> getType() {
        return ParticleRegistry.SHOCKWAVE_PARTICLE.get();
    }
}