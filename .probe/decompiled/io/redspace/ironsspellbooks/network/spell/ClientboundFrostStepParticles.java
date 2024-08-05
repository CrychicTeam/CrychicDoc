package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundFrostStepParticles {

    private Vec3 pos1;

    private Vec3 pos2;

    public ClientboundFrostStepParticles(Vec3 pos1, Vec3 pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public ClientboundFrostStepParticles(FriendlyByteBuf buf) {
        this.pos1 = this.readVec3(buf);
        this.pos2 = this.readVec3(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        this.writeVec3(this.pos1, buf);
        this.writeVec3(this.pos2, buf);
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
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleClientboundFrostStep(this.pos1, this.pos2));
        return true;
    }
}