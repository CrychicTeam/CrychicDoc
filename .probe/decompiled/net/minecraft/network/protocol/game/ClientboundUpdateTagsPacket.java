package net.minecraft.network.protocol.game;

import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagNetworkSerialization;

public class ClientboundUpdateTagsPacket implements Packet<ClientGamePacketListener> {

    private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;

    public ClientboundUpdateTagsPacket(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> mapResourceKeyExtendsRegistryTagNetworkSerializationNetworkPayload0) {
        this.tags = mapResourceKeyExtendsRegistryTagNetworkSerializationNetworkPayload0;
    }

    public ClientboundUpdateTagsPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.tags = friendlyByteBuf0.readMap(p_179484_ -> ResourceKey.createRegistryKey(p_179484_.readResourceLocation()), TagNetworkSerialization.NetworkPayload::m_203969_);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeMap(this.tags, (p_179480_, p_179481_) -> p_179480_.writeResourceLocation(p_179481_.location()), (p_206653_, p_206654_) -> p_206654_.write(p_206653_));
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleUpdateTags(this);
    }

    public Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> getTags() {
        return this.tags;
    }
}