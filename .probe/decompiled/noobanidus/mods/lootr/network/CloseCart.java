package noobanidus.mods.lootr.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import noobanidus.mods.lootr.network.client.ClientHandlers;

public class CloseCart {

    public int entityId;

    public CloseCart(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public CloseCart(int entityId) {
        this.entityId = entityId;
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(CloseCart message, Supplier<NetworkEvent.Context> context) {
        ClientHandlers.handleCloseCart(message, context);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> handle(this, context));
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}