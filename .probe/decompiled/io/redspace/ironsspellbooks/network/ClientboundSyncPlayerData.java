package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncPlayerData {

    SyncedSpellData syncedSpellData;

    public ClientboundSyncPlayerData(SyncedSpellData playerSyncedData) {
        this.syncedSpellData = playerSyncedData;
    }

    public ClientboundSyncPlayerData(FriendlyByteBuf buf) {
        this.syncedSpellData = SyncedSpellData.SYNCED_SPELL_DATA.read(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        SyncedSpellData.SYNCED_SPELL_DATA.write(buf, this.syncedSpellData);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.handlePlayerSyncedData(this.syncedSpellData));
        return true;
    }
}