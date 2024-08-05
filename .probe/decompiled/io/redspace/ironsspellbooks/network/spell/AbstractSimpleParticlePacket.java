package io.redspace.ironsspellbooks.network.spell;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public abstract class AbstractSimpleParticlePacket extends AbstractVec3Packet {

    public AbstractSimpleParticlePacket(Vec3 pos) {
        super(pos);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> this.particleFunction().accept(this.pos));
        return true;
    }

    abstract Consumer<Vec3> particleFunction();
}