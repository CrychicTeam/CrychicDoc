package com.simibubi.create.content.kinetics.base;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import java.util.Locale;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RotationIndicatorParticleData implements ParticleOptions, ICustomParticleDataWithSprite<RotationIndicatorParticleData> {

    public static final PrimitiveCodec<Character> CHAR = new PrimitiveCodec<Character>() {

        public <T> DataResult<Character> read(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(n -> (char) n.shortValue());
        }

        public <T> T write(DynamicOps<T> ops, Character value) {
            return (T) ops.createShort((short) value.charValue());
        }

        public String toString() {
            return "Char";
        }
    };

    public static final Codec<RotationIndicatorParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT.fieldOf("color").forGetter(p -> p.color), Codec.FLOAT.fieldOf("speed").forGetter(p -> p.speed), Codec.FLOAT.fieldOf("radius1").forGetter(p -> p.radius1), Codec.FLOAT.fieldOf("radius2").forGetter(p -> p.radius2), Codec.INT.fieldOf("lifeSpan").forGetter(p -> p.lifeSpan), CHAR.fieldOf("axis").forGetter(p -> p.axis)).apply(i, RotationIndicatorParticleData::new));

    public static final ParticleOptions.Deserializer<RotationIndicatorParticleData> DESERIALIZER = new ParticleOptions.Deserializer<RotationIndicatorParticleData>() {

        public RotationIndicatorParticleData fromCommand(ParticleType<RotationIndicatorParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            float speed = (float) reader.readDouble();
            reader.expect(' ');
            float rad1 = (float) reader.readDouble();
            reader.expect(' ');
            float rad2 = (float) reader.readDouble();
            reader.expect(' ');
            int lifeSpan = reader.readInt();
            reader.expect(' ');
            char axis = reader.read();
            return new RotationIndicatorParticleData(color, speed, rad1, rad2, lifeSpan, axis);
        }

        public RotationIndicatorParticleData fromNetwork(ParticleType<RotationIndicatorParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new RotationIndicatorParticleData(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readChar());
        }
    };

    final int color;

    final float speed;

    final float radius1;

    final float radius2;

    final int lifeSpan;

    final char axis;

    public RotationIndicatorParticleData(int color, float speed, float radius1, float radius2, int lifeSpan, char axis) {
        this.color = color;
        this.speed = speed;
        this.radius1 = radius1;
        this.radius2 = radius2;
        this.lifeSpan = lifeSpan;
        this.axis = axis;
    }

    public RotationIndicatorParticleData() {
        this(0, 0.0F, 0.0F, 0.0F, 0, '0');
    }

    @Override
    public ParticleType<?> getType() {
        return AllParticleTypes.ROTATION_INDICATOR.get();
    }

    public Direction.Axis getAxis() {
        return Direction.Axis.valueOf(this.axis + "");
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.color);
        buffer.writeFloat(this.speed);
        buffer.writeFloat(this.radius1);
        buffer.writeFloat(this.radius2);
        buffer.writeInt(this.lifeSpan);
        buffer.writeChar(this.axis);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %.2f %.2f %.2f %d %c", AllParticleTypes.ROTATION_INDICATOR.parameter(), this.color, this.speed, this.radius1, this.radius2, this.lifeSpan, this.axis);
    }

    @Override
    public ParticleOptions.Deserializer<RotationIndicatorParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<RotationIndicatorParticleData> getCodec(ParticleType<RotationIndicatorParticleData> type) {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ParticleEngine.SpriteParticleRegistration<RotationIndicatorParticleData> getMetaFactory() {
        return RotationIndicatorParticle.Factory::new;
    }
}