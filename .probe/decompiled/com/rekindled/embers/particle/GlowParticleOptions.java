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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GlowParticleOptions implements ParticleOptions {

    public static final float MIN_SCALE = 0.01F;

    public static final float MAX_SCALE = 4.0F;

    protected final Vector3f color;

    protected final Vec3 motion;

    protected final float scale;

    protected final int lifetime;

    public static final Vector3f EMBER_COLOR = new Vector3f(1.0F, 0.2509804F, 0.0627451F);

    public static final GlowParticleOptions EMBER = new GlowParticleOptions(EMBER_COLOR, 2.0F);

    public static final GlowParticleOptions EMBER_NOMOTION = new GlowParticleOptions(EMBER_COLOR, new Vec3(0.0, 1.0E-6, 0.0), 2.0F);

    public static final Codec<GlowParticleOptions> CODEC = RecordCodecBuilder.create(p_175793_ -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_175797_ -> p_175797_.color), Vec3.CODEC.fieldOf("motion").forGetter(p_175797_ -> p_175797_.motion), Codec.FLOAT.fieldOf("scale").forGetter(p_175795_ -> p_175795_.scale), Codec.INT.fieldOf("lifetime").forGetter(p_175795_ -> p_175795_.lifetime)).apply(p_175793_, GlowParticleOptions::new));

    public static final ParticleOptions.Deserializer<GlowParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<GlowParticleOptions>() {

        public GlowParticleOptions fromCommand(ParticleType<GlowParticleOptions> p_123689_, StringReader p_123690_) throws CommandSyntaxException {
            Vector3f vector3fColor = GlowParticleOptions.readVector3f(p_123690_);
            p_123690_.expect(' ');
            Vec3 vec3Motion = GlowParticleOptions.readVec3(p_123690_);
            p_123690_.expect(' ');
            float f = p_123690_.readFloat();
            int s = p_123690_.readInt();
            return new GlowParticleOptions(vector3fColor, vec3Motion, f, s);
        }

        public GlowParticleOptions fromNetwork(ParticleType<GlowParticleOptions> p_123692_, FriendlyByteBuf p_123693_) {
            return new GlowParticleOptions(GlowParticleOptions.readVector3f(p_123693_), GlowParticleOptions.readVec3(p_123693_), p_123693_.readFloat(), p_123693_.readInt());
        }
    };

    public GlowParticleOptions(Vector3f pColor, Vec3 pMotion, float pScale, int plifetime) {
        this.color = pColor;
        this.motion = pMotion;
        this.scale = pScale;
        this.lifetime = plifetime;
    }

    public GlowParticleOptions(Vector3f pColor, Vec3 pMotion, float pScale) {
        this(pColor, pMotion, pScale, -1);
    }

    public GlowParticleOptions(Vector3f pColor, float pScale, int plifetime) {
        this(pColor, Vec3.ZERO, pScale, plifetime);
    }

    public GlowParticleOptions(Vector3f pColor, float pScale) {
        this(pColor, Vec3.ZERO, pScale);
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

    public static Vec3 readVec3(StringReader pStringInput) throws CommandSyntaxException {
        pStringInput.expect(' ');
        double f = pStringInput.readDouble();
        pStringInput.expect(' ');
        double f1 = pStringInput.readDouble();
        pStringInput.expect(' ');
        double f2 = pStringInput.readDouble();
        return new Vec3(f, f1, f2);
    }

    public static Vector3f readVector3f(FriendlyByteBuf pBuffer) {
        return new Vector3f(pBuffer.readFloat(), pBuffer.readFloat(), pBuffer.readFloat());
    }

    public static Vec3 readVec3(FriendlyByteBuf pBuffer) {
        return new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.color.x());
        pBuffer.writeFloat(this.color.y());
        pBuffer.writeFloat(this.color.z());
        pBuffer.writeDouble(this.motion.x());
        pBuffer.writeDouble(this.motion.y());
        pBuffer.writeDouble(this.motion.z());
        pBuffer.writeFloat(this.scale);
        pBuffer.writeInt(this.lifetime);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.motion.x(), this.motion.y(), this.motion.z(), this.scale);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public Vec3 getMotion() {
        return this.motion;
    }

    public float getScale() {
        return this.scale;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryManager.GLOW_PARTICLE.get();
    }
}