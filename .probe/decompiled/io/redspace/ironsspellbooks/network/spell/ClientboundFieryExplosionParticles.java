package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundFieryExplosionParticles {

    private final Vec3 pos1;

    private final float radius;

    public ClientboundFieryExplosionParticles(Vec3 pos1, float radius) {
        this.pos1 = pos1;
        this.radius = radius;
    }

    public ClientboundFieryExplosionParticles(FriendlyByteBuf buf) {
        this.pos1 = this.readVec3(buf);
        this.radius = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        this.writeVec3(this.pos1, buf);
        buf.writeFloat(this.radius);
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
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleClientboundFieryExplosion(this.pos1, this.radius));
        return true;
    }
}