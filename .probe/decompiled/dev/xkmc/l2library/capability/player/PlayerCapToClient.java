package dev.xkmc.l2library.capability.player;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class PlayerCapToClient extends SerialPacketBase {

    @SerialField
    public PlayerCapToClient.Action action;

    @SerialField
    public ResourceLocation holderID;

    @SerialField
    public CompoundTag tag;

    @SerialField
    public UUID playerID;

    @Deprecated
    public PlayerCapToClient() {
    }

    public <T extends PlayerCapabilityTemplate<T>> PlayerCapToClient(PlayerCapToClient.Action action, PlayerCapabilityHolder<T> holder, T handler) {
        this.action = action;
        this.holderID = holder.id;
        this.tag = (CompoundTag) action.server.apply(handler);
        this.playerID = handler.player.m_20148_();
    }

    public void handle(NetworkEvent.Context context) {
        if (this.action == PlayerCapToClient.Action.ALL || this.action == PlayerCapToClient.Action.CLONE || Proxy.getClientPlayer().m_6084_()) {
            PlayerCapabilityHolder<?> holder = (PlayerCapabilityHolder<?>) PlayerCapabilityHolder.INTERNAL_MAP.get(this.holderID);
            this.action.client.accept(holder, this);
        }
    }

    public static enum Action {

        ALL(m -> TagCodec.toTag(new CompoundTag(), m), (holder, packet) -> holder.cacheSet(packet.tag, false)), CLONE(m -> TagCodec.toTag(new CompoundTag(), m), (holder, packet) -> holder.cacheSet(packet.tag, true)), TRACK(m -> TagCodec.toTag(new CompoundTag(), m.getClass(), m, SerialField::toTracking), (holder, packet) -> holder.updateTracked(packet.tag, Proxy.getClientWorld().m_46003_(packet.playerID)));

        public final Function<Object, CompoundTag> server;

        public final BiConsumer<PlayerCapabilityHolder<?>, PlayerCapToClient> client;

        private Action(Function<Object, CompoundTag> server, BiConsumer<PlayerCapabilityHolder<?>, PlayerCapToClient> client) {
            this.server = server;
            this.client = client;
        }
    }
}