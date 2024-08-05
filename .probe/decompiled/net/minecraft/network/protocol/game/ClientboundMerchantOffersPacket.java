package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.trading.MerchantOffers;

public class ClientboundMerchantOffersPacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final MerchantOffers offers;

    private final int villagerLevel;

    private final int villagerXp;

    private final boolean showProgress;

    private final boolean canRestock;

    public ClientboundMerchantOffersPacket(int int0, MerchantOffers merchantOffers1, int int2, int int3, boolean boolean4, boolean boolean5) {
        this.containerId = int0;
        this.offers = merchantOffers1;
        this.villagerLevel = int2;
        this.villagerXp = int3;
        this.showProgress = boolean4;
        this.canRestock = boolean5;
    }

    public ClientboundMerchantOffersPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readVarInt();
        this.offers = MerchantOffers.createFromStream(friendlyByteBuf0);
        this.villagerLevel = friendlyByteBuf0.readVarInt();
        this.villagerXp = friendlyByteBuf0.readVarInt();
        this.showProgress = friendlyByteBuf0.readBoolean();
        this.canRestock = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.containerId);
        this.offers.writeToStream(friendlyByteBuf0);
        friendlyByteBuf0.writeVarInt(this.villagerLevel);
        friendlyByteBuf0.writeVarInt(this.villagerXp);
        friendlyByteBuf0.writeBoolean(this.showProgress);
        friendlyByteBuf0.writeBoolean(this.canRestock);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleMerchantOffers(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    public int getVillagerLevel() {
        return this.villagerLevel;
    }

    public int getVillagerXp() {
        return this.villagerXp;
    }

    public boolean showProgress() {
        return this.showProgress;
    }

    public boolean canRestock() {
        return this.canRestock;
    }
}