package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundGuidingBoltManagerStartTracking {

    private final UUID entity;

    private final List<Integer> projectileIds;

    public ClientboundGuidingBoltManagerStartTracking(Entity entity, List<Projectile> projectiles) {
        this.entity = entity.getUUID();
        this.projectileIds = projectiles.stream().map(Entity::m_19879_).toList();
    }

    public ClientboundGuidingBoltManagerStartTracking(FriendlyByteBuf buf) {
        this.projectileIds = new ArrayList();
        this.entity = buf.readUUID();
        int i = buf.readInt();
        for (int j = 0; j < i; j++) {
            this.projectileIds.add(buf.readInt());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.entity);
        buf.writeInt(this.projectileIds.size());
        for (Integer projectileId : this.projectileIds) {
            buf.writeInt(projectileId);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> GuidingBoltManager.handleClientboundStartTracking(this.entity, this.projectileIds));
        return true;
    }
}