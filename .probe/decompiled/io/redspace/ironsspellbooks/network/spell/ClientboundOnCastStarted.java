package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundOnCastStarted {

    private String spellId;

    private int spellLevel;

    private UUID castingEntityId;

    public ClientboundOnCastStarted(UUID castingEntityId, String spellId, int spellLevel) {
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.castingEntityId = castingEntityId;
    }

    public ClientboundOnCastStarted(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.spellLevel = buf.readInt();
        this.castingEntityId = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.spellLevel);
        buf.writeUUID(this.castingEntityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleClientBoundOnCastStarted(this.castingEntityId, this.spellId, this.spellLevel));
        return true;
    }
}