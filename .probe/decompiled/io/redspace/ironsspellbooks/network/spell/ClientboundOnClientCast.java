package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundOnClientCast {

    String spellId;

    int level;

    CastSource castSource;

    ICastData castData;

    public ClientboundOnClientCast(String spellId, int level, CastSource castSource, ICastData castData) {
        this.spellId = spellId;
        this.level = level;
        this.castSource = castSource;
        this.castData = castData;
    }

    public ClientboundOnClientCast(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.level = buf.readInt();
        this.castSource = buf.readEnum(CastSource.class);
        if (buf.readBoolean()) {
            ICastDataSerializable tmp = SpellRegistry.getSpell(this.spellId).getEmptyCastData();
            tmp.readFromBuffer(buf);
            this.castData = tmp;
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.level);
        buf.writeEnum(this.castSource);
        if (this.castData instanceof ICastDataSerializable castDataSerializable) {
            buf.writeBoolean(true);
            castDataSerializable.writeToBuffer(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSpellCastHelper.handleClientboundOnClientCast(this.spellId, this.level, this.castSource, this.castData)));
        return true;
    }
}