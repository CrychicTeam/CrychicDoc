package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundGuidingBoltManagerStopTracking {

    private final UUID entity;

    public ClientboundGuidingBoltManagerStopTracking(Entity entity) {
        this.entity = entity.getUUID();
    }

    public ClientboundGuidingBoltManagerStopTracking(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.entity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> GuidingBoltManager.handleClientboundStopTracking(this.entity));
        return true;
    }
}