package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientBoundRemoveRecast {

    private final String spellId;

    public ClientBoundRemoveRecast(String spellId) {
        this.spellId = spellId;
    }

    public ClientBoundRemoveRecast(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.getRecasts().removeRecast(this.spellId));
        return true;
    }
}