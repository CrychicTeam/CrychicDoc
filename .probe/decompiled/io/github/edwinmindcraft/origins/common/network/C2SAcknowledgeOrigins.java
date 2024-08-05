package io.github.edwinmindcraft.origins.common.network;

import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class C2SAcknowledgeOrigins {

    public static C2SAcknowledgeOrigins decode(FriendlyByteBuf buf) {
        return new C2SAcknowledgeOrigins();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> IOriginContainer.get(((NetworkEvent.Context) contextSupplier.get()).getSender()).ifPresent(IOriginContainer::validateSynchronization));
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
    }
}