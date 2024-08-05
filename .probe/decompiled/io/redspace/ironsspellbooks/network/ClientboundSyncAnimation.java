package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncAnimation<T extends Entity & IAnimatedAttacker> {

    int entityId;

    String animationId;

    public ClientboundSyncAnimation(String animationId, T entity) {
        this.entityId = entity.getId();
        this.animationId = animationId;
    }

    public ClientboundSyncAnimation(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.animationId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.animationId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                if (level.getEntity(this.entityId) instanceof IAnimatedAttacker animatedAttacker) {
                    animatedAttacker.playAnimation(this.animationId);
                }
            }
        });
        return true;
    }
}