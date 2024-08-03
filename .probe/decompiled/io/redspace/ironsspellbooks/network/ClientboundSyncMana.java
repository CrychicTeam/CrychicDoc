package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncMana {

    private int playerMana = 0;

    private MagicData playerMagicData = null;

    public ClientboundSyncMana(MagicData playerMagicData) {
        this.playerMagicData = playerMagicData;
    }

    public ClientboundSyncMana(FriendlyByteBuf buf) {
        this.playerMana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt((int) this.playerMagicData.getMana());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.setMana(this.playerMana));
        return true;
    }
}