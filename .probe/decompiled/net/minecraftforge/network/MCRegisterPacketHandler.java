package net.minecraftforge.network;

import com.mojang.logging.LogUtils;
import io.netty.buffer.Unpooled;
import io.netty.util.Attribute;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

public class MCRegisterPacketHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MCRegisterPacketHandler INSTANCE = new MCRegisterPacketHandler();

    public void addChannels(Set<ResourceLocation> locations, Connection manager) {
        getFrom(manager).locations.addAll(locations);
    }

    void registerListener(NetworkEvent evt) {
        MCRegisterPacketHandler.ChannelList channelList = getFrom(evt);
        channelList.updateFrom(evt.getSource(), evt.getPayload(), NetworkEvent.RegistrationChangeType.REGISTER);
        ((NetworkEvent.Context) evt.getSource().get()).setPacketHandled(true);
    }

    void unregisterListener(NetworkEvent evt) {
        MCRegisterPacketHandler.ChannelList channelList = getFrom(evt);
        channelList.updateFrom(evt.getSource(), evt.getPayload(), NetworkEvent.RegistrationChangeType.UNREGISTER);
        ((NetworkEvent.Context) evt.getSource().get()).setPacketHandled(true);
    }

    private static MCRegisterPacketHandler.ChannelList getFrom(Connection manager) {
        return fromAttr(manager.channel().attr(NetworkConstants.FML_MC_REGISTRY));
    }

    private static MCRegisterPacketHandler.ChannelList getFrom(NetworkEvent event) {
        return fromAttr(((NetworkEvent.Context) event.getSource().get()).attr(NetworkConstants.FML_MC_REGISTRY));
    }

    private static MCRegisterPacketHandler.ChannelList fromAttr(Attribute<MCRegisterPacketHandler.ChannelList> attr) {
        attr.setIfAbsent(new MCRegisterPacketHandler.ChannelList());
        return (MCRegisterPacketHandler.ChannelList) attr.get();
    }

    public void sendRegistry(Connection manager, NetworkDirection dir) {
        FriendlyByteBuf pb = new FriendlyByteBuf(Unpooled.buffer());
        pb.writeBytes(getFrom(manager).toByteArray());
        ICustomPacket<Packet<?>> iPacketICustomPacket = dir.buildPacket(Pair.of(pb, 0), NetworkConstants.MC_REGISTER_RESOURCE);
        manager.send(iPacketICustomPacket.getThis());
    }

    public static class ChannelList {

        private Set<ResourceLocation> locations = new HashSet();

        private Set<ResourceLocation> remoteLocations = Set.of();

        public void updateFrom(Supplier<NetworkEvent.Context> source, FriendlyByteBuf buffer, NetworkEvent.RegistrationChangeType changeType) {
            byte[] data = new byte[Math.max(buffer.readableBytes(), 0)];
            buffer.readBytes(data);
            Set<ResourceLocation> oldLocations = this.locations;
            this.locations = this.bytesToResLocation(data);
            this.remoteLocations = Set.copyOf(this.locations);
            oldLocations.addAll(this.locations);
            oldLocations.stream().map(NetworkRegistry::findTarget).filter(Optional::isPresent).map(Optional::get).forEach(t -> t.dispatchEvent(new NetworkEvent.ChannelRegistrationChangeEvent(source, changeType)));
        }

        byte[] toByteArray() {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (ResourceLocation rl : this.locations) {
                try {
                    bos.write(rl.toString().getBytes(StandardCharsets.UTF_8));
                    bos.write(0);
                } catch (IOException var5) {
                }
            }
            return bos.toByteArray();
        }

        private Set<ResourceLocation> bytesToResLocation(byte[] all) {
            HashSet<ResourceLocation> rl = new HashSet();
            int last = 0;
            for (int cur = 0; cur < all.length; cur++) {
                if (all[cur] == 0) {
                    String s = new String(all, last, cur - last, StandardCharsets.UTF_8);
                    try {
                        rl.add(new ResourceLocation(s));
                    } catch (ResourceLocationException var7) {
                        MCRegisterPacketHandler.LOGGER.warn("Invalid channel name received: {}. Ignoring", s);
                    }
                    last = cur + 1;
                }
            }
            return rl;
        }

        public Set<ResourceLocation> getRemoteLocations() {
            return this.remoteLocations;
        }
    }
}