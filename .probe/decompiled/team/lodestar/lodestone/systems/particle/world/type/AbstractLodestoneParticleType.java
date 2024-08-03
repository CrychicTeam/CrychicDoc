package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class AbstractLodestoneParticleType<T extends WorldParticleOptions> extends ParticleType<T> {

    public AbstractLodestoneParticleType() {
        super(false, new ParticleOptions.Deserializer<T>() {

            public T fromCommand(ParticleType<T> type, StringReader reader) {
                return (T) (new WorldParticleOptions(type));
            }

            public T fromNetwork(ParticleType<T> type, FriendlyByteBuf buf) {
                return (T) (new WorldParticleOptions(type));
            }
        });
    }

    @Override
    public Codec<T> codec() {
        return genericCodec(this);
    }

    public static <K extends WorldParticleOptions> Codec<K> genericCodec(ParticleType<K> type) {
        return Codec.unit(() -> new WorldParticleOptions(type));
    }
}