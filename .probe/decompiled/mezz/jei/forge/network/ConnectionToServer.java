package mezz.jei.forge.network;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.UUID;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketJei;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public final class ConnectionToServer implements IConnectionToServer {

    @Nullable
    private static UUID jeiOnServerCacheUuid = null;

    private static boolean jeiOnServerCacheValue = false;

    private final NetworkHandler networkHandler;

    public ConnectionToServer(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    @Override
    public boolean isJeiOnServer() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener clientPacketListener = minecraft.getConnection();
        if (clientPacketListener == null) {
            return false;
        } else {
            UUID id = clientPacketListener.getId();
            if (!id.equals(jeiOnServerCacheUuid)) {
                jeiOnServerCacheUuid = id;
                jeiOnServerCacheValue = (Boolean) Optional.of(clientPacketListener).map(ClientPacketListener::m_104910_).map(NetworkHooks::getConnectionData).map(ConnectionData::getChannels).map(ImmutableMap::keySet).map(keys -> keys.contains(this.networkHandler.getChannelId())).orElse(false);
            }
            return jeiOnServerCacheValue;
        }
    }

    @Override
    public void sendPacketToServer(PacketJei packet) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener netHandler = minecraft.getConnection();
        if (netHandler != null && this.isJeiOnServer()) {
            Pair<FriendlyByteBuf, Integer> packetData = packet.getPacketData();
            ICustomPacket<Packet<?>> payload = NetworkDirection.PLAY_TO_SERVER.buildPacket(packetData, this.networkHandler.getChannelId());
            netHandler.send(payload.getThis());
        }
    }
}