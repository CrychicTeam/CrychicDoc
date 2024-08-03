package net.mehvahdjukaar.supplementaries.common.network;

import java.util.Map;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.network.FriendlyByteBuf;

public class ClientBoundSendLoginPacket implements Message {

    public final Map<UUID, String> usernameCache;

    public ClientBoundSendLoginPacket(FriendlyByteBuf buf) {
        this.usernameCache = buf.readMap(FriendlyByteBuf::m_130259_, FriendlyByteBuf::m_130277_);
    }

    public ClientBoundSendLoginPacket(Map<UUID, String> usernameCache) {
        this.usernameCache = usernameCache;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeMap(this.usernameCache, FriendlyByteBuf::m_130077_, FriendlyByteBuf::m_130070_);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ClientReceivers.handleLoginPacket(this);
    }
}