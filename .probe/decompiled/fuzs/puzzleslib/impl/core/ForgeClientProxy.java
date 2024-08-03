package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ForgeClientProxy extends ForgeServerProxy implements ClientProxyImpl {

    @Override
    public <T extends Record & ClientboundMessage<T>> void registerClientReceiverV2(T message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = (NetworkEvent.Context) supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            Objects.requireNonNull(player, "player is null");
            ((ClientMessageListener) message.getHandler()).handle(message, minecraft, player.connection, player, minecraft.level);
        });
        context.setPacketHandled(true);
    }
}