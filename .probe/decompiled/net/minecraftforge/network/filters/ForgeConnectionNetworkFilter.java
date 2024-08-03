package net.minecraftforge.network.filters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import io.netty.channel.ChannelHandler.Sharable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import org.jetbrains.annotations.Nullable;

@Sharable
public class ForgeConnectionNetworkFilter extends VanillaPacketFilter {

    public ForgeConnectionNetworkFilter(@Nullable Connection manager) {
        super(buildHandlers(manager));
    }

    private static Map<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> buildHandlers(@Nullable Connection manager) {
        VanillaPacketSplitter.RemoteCompatibility compatibility = manager == null ? VanillaPacketSplitter.RemoteCompatibility.ABSENT : VanillaPacketSplitter.getRemoteCompatibility(manager);
        if (compatibility == VanillaPacketSplitter.RemoteCompatibility.ABSENT) {
            return ImmutableMap.of();
        } else {
            Builder<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> builder = ImmutableMap.builder().put(ClientboundUpdateRecipesPacket.class, ForgeConnectionNetworkFilter::splitPacket).put(ClientboundUpdateTagsPacket.class, ForgeConnectionNetworkFilter::splitPacket).put(ClientboundUpdateAdvancementsPacket.class, ForgeConnectionNetworkFilter::splitPacket).put(ClientboundLoginPacket.class, ForgeConnectionNetworkFilter::splitPacket);
            return builder.build();
        }
    }

    @Override
    protected boolean isNecessary(Connection manager) {
        return !manager.isMemoryConnection() && VanillaPacketSplitter.isRemoteCompatible(manager);
    }

    private static void splitPacket(Packet<?> packet, List<? super Packet<?>> out) {
        VanillaPacketSplitter.appendPackets(ConnectionProtocol.PLAY, PacketFlow.CLIENTBOUND, packet, out);
    }
}