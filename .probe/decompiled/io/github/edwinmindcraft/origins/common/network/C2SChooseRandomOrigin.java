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

public record C2SChooseRandomOrigin(ResourceLocation layer) {

    public static C2SChooseRandomOrigin decode(FriendlyByteBuf buf) {
        return new C2SChooseRandomOrigin(buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.layer());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) contextSupplier.get()).getSender();
            if (sender != null) {
                IOriginContainer.get(sender).ifPresent(container -> {
                    Optional<Holder.Reference<OriginLayer>> layer = OriginsAPI.getLayersRegistry().getHolder(ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, this.layer())).filter(Holder::m_203633_);
                    if (layer.isEmpty()) {
                        Origins.LOGGER.warn("Player {} tried to select a random origin for missing layer {}", sender.m_6302_(), this.layer());
                    } else if (!container.hasAllOrigins() && !container.hasOrigin((Holder<OriginLayer>) layer.get())) {
                        Optional<Holder<Origin>> selected = ((OriginLayer) ((Holder.Reference) layer.get()).value()).selectRandom(sender);
                        if (((OriginLayer) ((Holder.Reference) layer.get()).value()).allowRandom() && !selected.isEmpty()) {
                            Holder<Origin> origin = (Holder<Origin>) selected.get();
                            boolean hadOriginBefore = container.hadAllOrigins();
                            boolean hadAllOrigins = container.hasAllOrigins();
                            container.setOrigin((Holder<OriginLayer>) layer.get(), origin);
                            container.checkAutoChoosingLayers(false);
                            container.synchronize();
                            if (container.hasAllOrigins() && !hadAllOrigins) {
                                container.onChosen(hadOriginBefore);
                            }
                            Origins.LOGGER.info("Player {} was randomly assigned the following Origin: \"{}\" for layer: {}", sender.m_6302_(), origin.unwrapKey().orElse(null), this.layer());
                            OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new S2CConfirmOrigin(this.layer(), (ResourceLocation) origin.unwrapKey().map(ResourceKey::m_135782_).orElse(null)));
                        } else {
                            Origins.LOGGER.warn("Player {} tried to choose a random Origin for layer {}, which is not allowed!", sender.m_6302_(), this.layer());
                            container.setOrigin((Holder<OriginLayer>) layer.get(), (Holder<Origin>) OriginRegisters.EMPTY.getHolder().orElseThrow());
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