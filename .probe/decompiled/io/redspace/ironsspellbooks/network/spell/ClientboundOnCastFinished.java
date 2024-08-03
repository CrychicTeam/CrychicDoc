package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundOnCastFinished {

    private final String spellId;

    private final UUID castingEntityId;

    private final boolean cancelled;

    public ClientboundOnCastFinished(UUID castingEntityId, String spellId, boolean cancelled) {
        this.spellId = spellId;
        this.castingEntityId = castingEntityId;
        this.cancelled = cancelled;
    }

    public ClientboundOnCastFinished(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.castingEntityId = buf.readUUID();
        this.cancelled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeUUID(this.castingEntityId);
        buf.writeBoolean(this.cancelled);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientSpellCastHelper.handleClientBoundOnCastFinished(this.castingEntityId, this.spellId, this.cancelled));
        return true;
    }
}