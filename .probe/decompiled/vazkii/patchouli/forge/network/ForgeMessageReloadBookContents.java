package vazkii.patchouli.forge.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ForgeMessageReloadBookContents {

    public static void sendToAll(MinecraftServer server) {
        ForgeNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new ForgeMessageReloadBookContents());
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static ForgeMessageReloadBookContents decode(FriendlyByteBuf buf) {
        return new ForgeMessageReloadBookContents();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> ClientBookRegistry.INSTANCE.reload());
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}