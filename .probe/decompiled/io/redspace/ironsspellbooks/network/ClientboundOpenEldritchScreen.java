package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundOpenEldritchScreen {

    private final InteractionHand hand;

    public ClientboundOpenEldritchScreen(InteractionHand pHand) {
        this.hand = pHand;
    }

    public ClientboundOpenEldritchScreen(FriendlyByteBuf buf) {
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientSpellCastHelper.openEldritchResearchScreen(this.hand));
        return true;
    }
}