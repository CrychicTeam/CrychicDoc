package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundUpdateCastingState {

    private final String spellId;

    private final int spellLevel;

    private final int castTime;

    private final CastSource castSource;

    private final String castingEquipmentSlot;

    public ClientboundUpdateCastingState(String spellId, int spellLevel, int castTime, CastSource castSource, String castingEquipmentSlot) {
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.castTime = castTime;
        this.castSource = castSource;
        this.castingEquipmentSlot = castingEquipmentSlot;
    }

    public ClientboundUpdateCastingState(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.spellLevel = buf.readInt();
        this.castTime = buf.readInt();
        this.castSource = buf.readEnum(CastSource.class);
        this.castingEquipmentSlot = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.spellLevel);
        buf.writeInt(this.castTime);
        buf.writeEnum(this.castSource);
        buf.writeUtf(this.castingEquipmentSlot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.setClientCastState(this.spellId, this.spellLevel, this.castTime, this.castSource, this.castingEquipmentSlot));
        return true;
    }
}