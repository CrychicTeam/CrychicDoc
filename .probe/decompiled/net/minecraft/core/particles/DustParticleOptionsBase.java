package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Locale;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public abstract class DustParticleOptionsBase implements ParticleOptions {

    public static final float MIN_SCALE = 0.01F;

    public static final float MAX_SCALE = 4.0F;

    protected final Vector3f color;

    protected final float scale;

    public DustParticleOptionsBase(Vector3f vectorF0, float float1) {
        this.color = vectorF0;
        this.scale = Mth.clamp(float1, 0.01F, 4.0F);
    }

    public static Vector3f readVector3f(StringReader stringReader0) throws CommandSyntaxException {
        stringReader0.expect(' ');
        float $$1 = stringReader0.readFloat();
        stringReader0.expect(' ');
        float $$2 = stringReader0.readFloat();
        stringReader0.expect(' ');
        float $$3 = stringReader0.readFloat();
        return new Vector3f($$1, $$2, $$3);
    }

    public static Vector3f readVector3f(FriendlyByteBuf friendlyByteBuf0) {
        return new Vector3f(friendlyByteBuf0.readFloat(), friendlyByteBuf0.readFloat(), friendlyByteBuf0.readFloat());
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeFloat(this.color.x());
        friendlyByteBuf0.writeFloat(this.color.y());
        friendlyByteBuf0.writeFloat(this.color.z());
        friendlyByteBuf0.writeFloat(this.scale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.m_6012_()), this.color.x(), this.color.y(), this.color.z(), this.scale);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }
}