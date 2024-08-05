package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientboundParticleShockwave {

    Vec3 pos;

    float radius;

    String particleName;

    public ClientboundParticleShockwave(Vec3 pos, float radius, ParticleType particleType) {
        this.pos = pos;
        this.radius = radius;
        this.particleName = ((ResourceLocation) Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(particleType))).toString();
    }

    public ClientboundParticleShockwave(FriendlyByteBuf buf) {
        this.pos = this.readVec3(buf);
        this.radius = buf.readFloat();
        this.particleName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        this.writeVec3(this.pos, buf);
        buf.writeFloat(this.radius);
        buf.writeUtf(this.particleName);
    }

    public Vec3 readVec3(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }

    public void writeVec3(Vec3 vec3, FriendlyByteBuf buf) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            try {
                ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(this.particleName));
                ClientSpellCastHelper.handleClientboundShockwaveParticle(this.pos, this.radius, type);
            } catch (Exception var2x) {
            }
        });
        return true;
    }
}