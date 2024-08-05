package io.github.edwinmindcraft.origins.common.network;

import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public record C2SChooseOrigin(ResourceLocation layer, ResourceLocation origin) {

    public static C2SChooseOrigin decode(FriendlyByteBuf buf) {
        return new C2SChooseOrigin(buf.readResourceLocation(), buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.layer());
        buf.writeResourceLocation(this.origin());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) contextSupplier.get()).getSender();
            if (sender != null) {
                IOriginContainer.get(sender).ifPresent(container -> {
                    Optional<Holder.Reference<OriginLayer>> layer = OriginsAPI.getLayersRegistry().getHolder(ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, this.layer())).filter(Holder::m_203633_);
                    if (layer.isEmpty()) {
                        Origins.LOGGER.warn("Player {} tried to select an origin for missing layer {}", sender.m_6302_(), this.layer());
                    } else if (!container.hasAllOrigins() && !container.hasOrigin((Holder<OriginLayer>) layer.get())) {
                        Optional<Holder.Reference<Origin>> origin = OriginsAPI.getOriginsRegistry().getHolder(ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, this.origin())).filter(Holder::m_203633_);
                        if (origin.isEmpty()) {
                            Origins.LOGGER.warn("Player {} chose unknown origin: {} for layer {}", sender.m_6302_(), this.origin(), this.layer());
                        } else {
                            if (((Origin) ((Holder.Reference) origin.get()).value()).isChoosable() && ((OriginLayer) ((Holder.Reference) layer.get()).value()).contains(this.origin(), sender)) {
                                boolean hadOriginBefore = container.hadAllOrigins();
                                boolean hadAllOrigins = container.hasAllOrigins();
                                container.setOrigin((Holder<OriginLayer>) layer.get(), (Holder<Origin>) origin.get());
                                container.checkAutoChoosingLayers(false);
                                if (container.hasAllOrigins() && !hadAllOrigins) {
                                    container.onChosen(hadOriginBefore);
                                }
                            } else {
                                Origins.LOGGER.warn("Player {} tried to choose invalid origin: {} for layer: {}", sender.m_6302_(), this.origin(), this.layer());
                                container.setOrigin((Holder<OriginLayer>) layer.get(), (Holder<Origin>) OriginRegisters.EMPTY.getHolder().orElseThrow());
                            }
                            container.synchronize();
                            OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new S2CConfirmOrigin(this.layer(), this.origin()));
                        }
                    } else {
                        Origins.LOGGER.warn("Player {} tried to choose origin for layer {} while having one already.", sender.m_6302_(), this.layer());
                    }
                });
            }
        });
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
    }
}