package net.minecraft.network.protocol.game;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public record ClientboundUpdateEnabledFeaturesPacket(Set<ResourceLocation> f_244610_) implements Packet<ClientGamePacketListener> {

    private final Set<ResourceLocation> features;

    public ClientboundUpdateEnabledFeaturesPacket(FriendlyByteBuf p_250545_) {
        this(p_250545_.readCollection(HashSet::new, FriendlyByteBuf::m_130281_));
    }

    public ClientboundUpdateEnabledFeaturesPacket(Set<ResourceLocation> f_244610_) {
        this.features = f_244610_;
    }

    @Override
    public void write(FriendlyByteBuf p_251972_) {
        p_251972_.writeCollection(this.features, FriendlyByteBuf::m_130085_);
    }

    public void handle(ClientGamePacketListener p_250317_) {
        p_250317_.handleEnabledFeatures(this);
    }
}