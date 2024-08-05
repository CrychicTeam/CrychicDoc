package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundCastErrorMessage {

    public final ClientboundCastErrorMessage.ErrorType errorType;

    public final String spellId;

    public ClientboundCastErrorMessage(ClientboundCastErrorMessage.ErrorType errorType, AbstractSpell spell) {
        this.spellId = spell.getSpellId();
        this.errorType = errorType;
    }

    public ClientboundCastErrorMessage(FriendlyByteBuf buf) {
        this.errorType = buf.readEnum(ClientboundCastErrorMessage.ErrorType.class);
        this.spellId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(this.errorType);
        buf.writeUtf(this.spellId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleCastErrorMessage(this));
        return true;
    }

    public static enum ErrorType {

        COOLDOWN, MANA
    }
}