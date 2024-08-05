package noobanidus.mods.lootr.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import noobanidus.mods.lootr.network.client.ClientHandlers;

public class OpenCart {

    public int entityId;

    public OpenCart(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public OpenCart(int entityId) {
        this.entityId = entityId;
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(OpenCart message, Supplier<NetworkEvent.Context> context) {
        ClientHandlers.handleOpenCart(message, context);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> handle(this, context));
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}