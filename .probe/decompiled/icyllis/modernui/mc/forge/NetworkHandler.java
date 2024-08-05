package icyllis.modernui.mc.forge;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

public class NetworkHandler {

    protected final ResourceLocation mName;

    protected final String mProtocol;

    protected final boolean mOptional;

    public NetworkHandler(@Nonnull ResourceLocation name, @Nonnull String protocol, boolean optional) {
        this.mName = name;
        this.mProtocol = protocol;
        this.mOptional = optional;
        EventNetworkChannel channel = NetworkRegistry.newEventChannel(name, this::getProtocol, this::tryServerVersionOnClient, this::tryClientVersionOnServer);
        if (FMLEnvironment.dist.isClient()) {
            channel.addListener(this::onClientCustomPayload);
        }
        channel.addListener(this::onServerCustomPayload);
    }

    public String getProtocol() {
        return this.mProtocol;
    }

    protected boolean tryServerVersionOnClient(@Nonnull String protocol) {
        return this.mOptional && protocol.equals(NetworkRegistry.ABSENT) || this.mProtocol.equals(protocol);
    }

    protected boolean tryClientVersionOnServer(@Nonnull String protocol) {
        return this.mOptional && protocol.equals(NetworkRegistry.ABSENT) || this.mProtocol.equals(protocol);
    }

    @OnlyIn(Dist.CLIENT)
    protected void onClientCustomPayload(@Nonnull NetworkEvent.ServerCustomPayloadEvent event) {
        FriendlyByteBuf payload = event.getPayload();
        LocalPlayer currentPlayer = Minecraft.getInstance().player;
        if (payload != null && event.getLoginIndex() == Integer.MAX_VALUE && currentPlayer != null) {
            this.handleClientMessage(payload.readUnsignedShort(), payload, event.getSource(), Minecraft.getInstance());
        }
        ((NetworkEvent.Context) event.getSource().get()).setPacketHandled(true);
    }

    protected void onServerCustomPayload(@Nonnull NetworkEvent.ClientCustomPayloadEvent event) {
        FriendlyByteBuf payload = event.getPayload();
        ServerPlayer currentPlayer = ((NetworkEvent.Context) event.getSource().get()).getSender();
        if (payload != null && event.getLoginIndex() == Integer.MAX_VALUE && currentPlayer != null) {
            this.handleServerMessage(payload.readUnsignedShort(), payload, event.getSource(), currentPlayer.server);
        }
        ((NetworkEvent.Context) event.getSource().get()).setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    protected void handleClientMessage(int index, @Nonnull FriendlyByteBuf payload, @Nonnull Supplier<NetworkEvent.Context> source, @Nonnull BlockableEventLoop<?> looper) {
    }

    protected void handleServerMessage(int index, @Nonnull FriendlyByteBuf payload, @Nonnull Supplier<NetworkEvent.Context> source, @Nonnull BlockableEventLoop<?> looper) {
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static LocalPlayer getClientPlayer(@Nonnull Supplier<NetworkEvent.Context> source) {
        return ((NetworkEvent.Context) source.get()).getNetworkManager().isConnected() ? Minecraft.getInstance().player : null;
    }

    @Nullable
    public static ServerPlayer getServerPlayer(@Nonnull Supplier<NetworkEvent.Context> source) {
        return ((NetworkEvent.Context) source.get()).getNetworkManager().isConnected() ? ((NetworkEvent.Context) source.get()).getSender() : null;
    }

    @Nonnull
    public PacketBuffer buffer(int index) {
        assert index >= 0 && index <= 65535;
        PacketBuffer buffer = new PacketBuffer(this.mName);
        buffer.writeShort(index);
        return buffer;
    }
}