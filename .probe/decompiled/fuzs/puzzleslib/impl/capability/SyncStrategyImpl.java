package fuzs.puzzleslib.impl.capability;

import fuzs.puzzleslib.api.capability.v2.data.SyncStrategy;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import java.util.function.BiConsumer;
import net.minecraft.server.level.ServerPlayer;

public final class SyncStrategyImpl implements SyncStrategy {

    private final BiConsumer<?, ServerPlayer> sender;

    public <T extends Record & ClientboundMessage<T>> SyncStrategyImpl(BiConsumer<T, ServerPlayer> sender) {
        this.sender = sender;
    }

    @Override
    public <T extends Record & ClientboundMessage<T>> void sendTo(T message, ServerPlayer player) {
        this.sender.accept(message, player);
    }
}