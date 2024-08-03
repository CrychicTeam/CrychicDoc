package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public record SculkChargeParticleOptions(float f_235914_) implements ParticleOptions {

    private final float roll;

    public static final Codec<SculkChargeParticleOptions> CODEC = RecordCodecBuilder.create(p_235920_ -> p_235920_.group(Codec.FLOAT.fieldOf("roll").forGetter(p_235922_ -> p_235922_.roll)).apply(p_235920_, SculkChargeParticleOptions::new));

    public static final ParticleOptions.Deserializer<SculkChargeParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<SculkChargeParticleOptions>() {

        public SculkChargeParticleOptions fromCommand(ParticleType<SculkChargeParticleOptions> p_235933_, StringReader p_235934_) throws CommandSyntaxException {
            p_235934_.expect(' ');
            float $$2 = p_235934_.readFloat();
            return new SculkChargeParticleOptions($$2);
        }

        public SculkChargeParticleOptions fromNetwork(ParticleType<SculkChargeParticleOptions> p_235936_, FriendlyByteBuf p_235937_) {
            return new SculkChargeParticleOptions(p_235937_.readFloat());
        }
    };

    public SculkChargeParticleOptions(float f_235914_) {
        this.roll = f_235914_;
    }

    @Override
    public ParticleType<SculkChargeParticleOptions> getType() {
        return ParticleTypes.SCULK_CHARGE;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf p_235924_) {
        p_235924_.writeFloat(this.roll);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.roll);
    }
}