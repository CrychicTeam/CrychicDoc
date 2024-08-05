package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ClientboundSelectAdvancementsTabPacket implements Packet<ClientGamePacketListener> {

    @Nullable
    private final ResourceLocation tab;

    public ClientboundSelectAdvancementsTabPacket(@Nullable ResourceLocation resourceLocation0) {
        this.tab = resourceLocation0;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSelectAdvancementsTab(this);
    }

    public ClientboundSelectAdvancementsTabPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.tab = friendlyByteBuf0.readNullable(FriendlyByteBuf::m_130281_);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeNullable(this.tab, FriendlyByteBuf::m_130085_);
    }

    @Nullable
    public ResourceLocation getTab() {
        return this.tab;
    }
}