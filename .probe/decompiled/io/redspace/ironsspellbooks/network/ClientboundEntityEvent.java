package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundEntityEvent<T extends Entity & IClientEventEntity> {

    private final int entityId;

    private final byte eventId;

    public ClientboundEntityEvent(Entity pEntity, byte pEventId) {
        this.entityId = pEntity.getId();
        this.eventId = pEventId;
    }

    public ClientboundEntityEvent(FriendlyByteBuf pBuffer) {
        this.entityId = pBuffer.readInt();
        this.eventId = pBuffer.readByte();
    }

    public void toBytes(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.entityId);
        pBuffer.writeByte(this.eventId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> MinecraftInstanceHelper.ifPlayerPresent(player -> {
            if (this.getEntity(player.f_19853_) instanceof IClientEventEntity entity) {
                entity.handleClientEvent(this.eventId);
            }
        }));
        return true;
    }

    @Nullable
    public Entity getEntity(Level pLevel) {
        return pLevel.getEntity(this.entityId);
    }

    public byte getEventId() {
        return this.eventId;
    }
}