package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ForgeServerProxy implements ForgeProxy {

    @Override
    public <T extends Record & ClientboundMessage<T>> void registerClientReceiverV2(T message, Supplier<NetworkEvent.Context> supplier) {
    }

    @Override
    public <T extends Record & ServerboundMessage<T>> void registerServerReceiverV2(T message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = (NetworkEvent.Context) supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Objects.requireNonNull(player, "player is null");
            ((ServerMessageListener) message.getHandler()).handle(message, CommonAbstractions.INSTANCE.getMinecraftServer(), player.connection, player, player.serverLevel());
        });
        context.setPacketHandled(true);
    }
}