package io.github.edwinmindcraft.origins.common.network;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.client.OriginsClientUtils;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.capabilities.OriginContainer;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public record S2CSynchronizeOrigin(int entity, Map<ResourceLocation, ResourceLocation> origins, boolean hadAllOrigins) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entity());
        buf.writeVarInt(this.origins().size());
        this.origins().forEach((layer, origin) -> {
            buf.writeResourceLocation(layer);
            buf.writeResourceLocation(origin);
        });
        buf.writeBoolean(this.hadAllOrigins());
    }

    public static S2CSynchronizeOrigin decode(FriendlyByteBuf buf) {
        int entity = buf.readInt();
        int size = buf.readVarInt();
        Builder<ResourceLocation, ResourceLocation> builder = ImmutableMap.builder();
        for (int i = 0; i < size; i++) {
            builder.put(buf.readResourceLocation(), buf.readResourceLocation());
        }
        boolean hadAll = buf.readBoolean();
        return new S2CSynchronizeOrigin(entity, builder.build(), hadAll);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
            Level level = (Level) DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> OriginsClientUtils::getClientLevel);
            if (level != null) {
                Entity entity = level.getEntity(this.entity());
                if (entity != null) {
                    entity.getCapability(OriginsAPI.ORIGIN_CONTAINER).ifPresent(x -> {
                        if (x instanceof OriginContainer container) {
                            container.acceptSynchronization(this.origins(), this.hadAllOrigins());
                            OriginsCommon.CHANNEL.send(PacketDistributor.SERVER.noArg(), new C2SAcknowledgeOrigins());
                        }
                    });
                }
            }
        });
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
    }
}