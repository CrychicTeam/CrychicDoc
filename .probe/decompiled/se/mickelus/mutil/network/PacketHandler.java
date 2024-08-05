package se.mickelus.mutil.network;

import java.util.ArrayList;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class PacketHandler {

    private static final Logger logger = LogManager.getLogger();

    private final SimpleChannel channel;

    private final ArrayList<Class<? extends AbstractPacket>> packets = new ArrayList();

    public PacketHandler(String namespace, String channelId, String protocolVersion) {
        this.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(namespace, channelId), () -> protocolVersion, protocolVersion::equals, protocolVersion::equals);
    }

    public <T extends AbstractPacket> boolean registerPacket(Class<T> packetClass, Supplier<T> supplier) {
        if (this.packets.size() > 256) {
            logger.warn("Attempted to register packet but packet list is full: " + packetClass.toString());
            return false;
        } else if (this.packets.contains(packetClass)) {
            logger.warn("Attempted to register packet but packet is already in list: " + packetClass.toString());
            return false;
        } else {
            this.channel.messageBuilder(packetClass, this.packets.size()).encoder(AbstractPacket::toBytes).decoder(buffer -> {
                T packet = (T) supplier.get();
                packet.fromBytes(buffer);
                return packet;
            }).consumerNetworkThread(this::onMessage).add();
            this.packets.add(packetClass);
            return true;
        }
    }

    public void onMessage(AbstractPacket message, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isServer()) {
                message.handle(((NetworkEvent.Context) ctx.get()).getSender());
            } else {
                message.handle(this.getClientPlayer());
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public void sendTo(AbstractPacket message, ServerPlayer player) {
        this.channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void sendToAllPlayers(AbstractPacket message) {
        this.channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public void sendToAllPlayersNear(AbstractPacket message, BlockPos pos, double r2, ResourceKey<Level> dim) {
        this.channel.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), r2, dim)), message);
    }

    @OnlyIn(Dist.CLIENT)
    public void sendToServer(AbstractPacket message) {
        if (Minecraft.getInstance().getConnection() != null) {
            this.channel.sendToServer(message);
        }
    }
}