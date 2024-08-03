package net.mehvahdjukaar.moonlight.api.platform.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Function;
import java.util.function.IntSupplier;
import net.mehvahdjukaar.moonlight.api.platform.network.forge.ChannelHandlerImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class ChannelHandler {

    protected final String name;

    public static ChannelHandler.Builder builder(String modId) {
        return new ChannelHandler.Builder(modId);
    }

    @Deprecated(forRemoval = true)
    public static ChannelHandler createChannel(ResourceLocation channelMame, int version) {
        return createChannel(channelMame.getNamespace(), () -> version);
    }

    @Deprecated(forRemoval = true)
    public static ChannelHandler createChannel(ResourceLocation channelMame) {
        return createChannel(channelMame, 1);
    }

    public static ChannelHandler createChannel(String modId) {
        return createChannel(modId, () -> 0);
    }

    @ExpectPlatform
    @Transformed
    public static ChannelHandler createChannel(String modId, IntSupplier version) {
        return ChannelHandlerImpl.createChannel(modId, version);
    }

    protected ChannelHandler(String modId) {
        this.name = modId;
    }

    @Deprecated
    public abstract <M extends Message> void register(NetworkDir var1, Class<M> var2, Function<FriendlyByteBuf, M> var3);

    public abstract void sendToClientPlayer(ServerPlayer var1, Message var2);

    public abstract void sendToAllClientPlayers(Message var1);

    public abstract void sendToAllClientPlayersInRange(Level var1, BlockPos var2, double var3, Message var5);

    public void sendToAllClientPlayersInDefaultRange(Level level, BlockPos pos, Message message) {
        this.sendToAllClientPlayersInRange(level, pos, 64.0, message);
    }

    public abstract void sentToAllClientPlayersTrackingEntity(Entity var1, Message var2);

    public abstract void sentToAllClientPlayersTrackingEntityAndSelf(Entity var1, Message var2);

    public abstract void sendToServer(Message var1);

    public static class Builder {

        private final ChannelHandler instance;

        private int version = 0;

        protected Builder(String modId) {
            this.instance = ChannelHandler.createChannel(modId, () -> this.version);
        }

        public <M extends Message> ChannelHandler.Builder register(NetworkDir direction, Class<M> messageClass, Function<FriendlyByteBuf, M> decoder) {
            this.instance.register(direction, messageClass, decoder);
            return this;
        }

        public ChannelHandler.Builder version(int version) {
            this.version = version;
            return this;
        }

        public ChannelHandler build() {
            return this.instance;
        }
    }

    public interface Context {

        NetworkDir getDirection();

        Player getSender();

        void disconnect(Component var1);
    }
}