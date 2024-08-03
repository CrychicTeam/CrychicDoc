package net.minecraft.network.protocol.game;

import java.util.List;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundPlayerInfoRemovePacket(List<UUID> f_244383_) implements Packet<ClientGamePacketListener> {

    private final List<UUID> profileIds;

    public ClientboundPlayerInfoRemovePacket(FriendlyByteBuf p_248744_) {
        this(p_248744_.readList(FriendlyByteBuf::m_130259_));
    }

    public ClientboundPlayerInfoRemovePacket(List<UUID> f_244383_) {
        this.profileIds = f_244383_;
    }

    @Override
    public void write(FriendlyByteBuf p_249263_) {
        p_249263_.writeCollection(this.profileIds, FriendlyByteBuf::m_130077_);
    }

    public void handle(ClientGamePacketListener p_250111_) {
        p_250111_.handlePlayerInfoRemove(this);
    }
}