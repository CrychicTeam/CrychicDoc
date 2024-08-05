package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;

public class VibrationParticleOption implements ParticleOptions {

    public static final Codec<VibrationParticleOption> CODEC = RecordCodecBuilder.create(p_235978_ -> p_235978_.group(PositionSource.CODEC.fieldOf("destination").forGetter(p_235982_ -> p_235982_.destination), Codec.INT.fieldOf("arrival_in_ticks").forGetter(p_235980_ -> p_235980_.arrivalInTicks)).apply(p_235978_, VibrationParticleOption::new));

    public static final ParticleOptions.Deserializer<VibrationParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<VibrationParticleOption>() {

        public VibrationParticleOption fromCommand(ParticleType<VibrationParticleOption> p_175859_, StringReader p_175860_) throws CommandSyntaxException {
            p_175860_.expect(' ');
            float $$2 = (float) p_175860_.readDouble();
            p_175860_.expect(' ');
            float $$3 = (float) p_175860_.readDouble();
            p_175860_.expect(' ');
            float $$4 = (float) p_175860_.readDouble();
            p_175860_.expect(' ');
            int $$5 = p_175860_.readInt();
            BlockPos $$6 = BlockPos.containing((double) $$2, (double) $$3, (double) $$4);
            return new VibrationParticleOption(new BlockPositionSource($$6), $$5);
        }

        public VibrationParticleOption fromNetwork(ParticleType<VibrationParticleOption> p_175862_, FriendlyByteBuf p_175863_) {
            PositionSource $$2 = PositionSourceType.fromNetwork(p_175863_);
            int $$3 = p_175863_.readVarInt();
            return new VibrationParticleOption($$2, $$3);
        }
    };

    private final PositionSource destination;

    private final int arrivalInTicks;

    public VibrationParticleOption(PositionSource positionSource0, int int1) {
        this.destination = positionSource0;
        this.arrivalInTicks = int1;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        PositionSourceType.toNetwork(this.destination, friendlyByteBuf0);
        friendlyByteBuf0.writeVarInt(this.arrivalInTicks);
    }

    @Override
    public String writeToString() {
        Vec3 $$0 = (Vec3) this.destination.getPosition(null).get();
        double $$1 = $$0.x();
        double $$2 = $$0.y();
        double $$3 = $$0.z();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), $$1, $$2, $$3, this.arrivalInTicks);
    }

    @Override
    public ParticleType<VibrationParticleOption> getType() {
        return ParticleTypes.VIBRATION;
    }

    public PositionSource getDestination() {
        return this.destination;
    }

    public int getArrivalInTicks() {
        return this.arrivalInTicks;
    }
}