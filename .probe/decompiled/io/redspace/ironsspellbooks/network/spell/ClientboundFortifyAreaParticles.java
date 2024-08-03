package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundFortifyAreaParticles {

    private Vec3 pos;

    public ClientboundFortifyAreaParticles(Vec3 pos) {
        this.pos = pos;
    }

    public ClientboundFortifyAreaParticles(FriendlyByteBuf buf) {
        this.pos = this.readVec3(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        this.writeVec3(this.pos, buf);
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
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleClientsideFortifyAreaParticles(this.pos));
        return true;
    }
}