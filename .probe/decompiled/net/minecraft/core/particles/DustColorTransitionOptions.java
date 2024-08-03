package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DustColorTransitionOptions extends DustParticleOptionsBase {

    public static final Vector3f SCULK_PARTICLE_COLOR = Vec3.fromRGB24(3790560).toVector3f();

    public static final DustColorTransitionOptions SCULK_TO_REDSTONE = new DustColorTransitionOptions(SCULK_PARTICLE_COLOR, DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0F);

    public static final Codec<DustColorTransitionOptions> CODEC = RecordCodecBuilder.create(p_253369_ -> p_253369_.group(ExtraCodecs.VECTOR3F.fieldOf("fromColor").forGetter(p_253368_ -> p_253368_.f_175800_), ExtraCodecs.VECTOR3F.fieldOf("toColor").forGetter(p_253367_ -> p_253367_.toColor), Codec.FLOAT.fieldOf("scale").forGetter(p_175765_ -> p_175765_.f_175801_)).apply(p_253369_, DustColorTransitionOptions::new));

    public static final ParticleOptions.Deserializer<DustColorTransitionOptions> DESERIALIZER = new ParticleOptions.Deserializer<DustColorTransitionOptions>() {

        public DustColorTransitionOptions fromCommand(ParticleType<DustColorTransitionOptions> p_175777_, StringReader p_175778_) throws CommandSyntaxException {
            Vector3f $$2 = DustParticleOptionsBase.readVector3f(p_175778_);
            p_175778_.expect(' ');
            float $$3 = p_175778_.readFloat();
            Vector3f $$4 = DustParticleOptionsBase.readVector3f(p_175778_);
            return new DustColorTransitionOptions($$2, $$4, $$3);
        }

        public DustColorTransitionOptions fromNetwork(ParticleType<DustColorTransitionOptions> p_175780_, FriendlyByteBuf p_175781_) {
            Vector3f $$2 = DustParticleOptionsBase.readVector3f(p_175781_);
            float $$3 = p_175781_.readFloat();
            Vector3f $$4 = DustParticleOptionsBase.readVector3f(p_175781_);
            return new DustColorTransitionOptions($$2, $$4, $$3);
        }
    };

    private final Vector3f toColor;

    public DustColorTransitionOptions(Vector3f vectorF0, Vector3f vectorF1, float float2) {
        super(vectorF0, float2);
        this.toColor = vectorF1;
    }

    public Vector3f getFromColor() {
        return this.f_175800_;
    }

    public Vector3f getToColor() {
        return this.toColor;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        super.writeToNetwork(friendlyByteBuf0);
        friendlyByteBuf0.writeFloat(this.toColor.x());
        friendlyByteBuf0.writeFloat(this.toColor.y());
        friendlyByteBuf0.writeFloat(this.toColor.z());
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.f_175800_.x(), this.f_175800_.y(), this.f_175800_.z(), this.f_175801_, this.toColor.x(), this.toColor.y(), this.toColor.z());
    }

    @Override
    public ParticleType<DustColorTransitionOptions> getType() {
        return ParticleTypes.DUST_COLOR_TRANSITION;
    }
}