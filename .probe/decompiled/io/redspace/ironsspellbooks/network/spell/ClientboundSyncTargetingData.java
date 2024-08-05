package io.redspace.ironsspellbooks.network.spell;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncTargetingData {

    private final List<UUID> targetUUIDs = new ArrayList();

    private final String spellId;

    public ClientboundSyncTargetingData(LivingEntity entity, AbstractSpell spell) {
        this.targetUUIDs.add(entity.m_20148_());
        this.spellId = spell.getSpellId();
    }

    public ClientboundSyncTargetingData(AbstractSpell spell, List<UUID> uuids) {
        this.targetUUIDs.addAll(uuids);
        this.spellId = spell.getSpellId();
    }

    public ClientboundSyncTargetingData(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        int i = buf.readInt();
        for (int j = 0; j < i; j++) {
            this.targetUUIDs.add(buf.readUUID());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        IronsSpellbooks.LOGGER.debug("ClientboundSyncTargetingData.toBytes: {} {}: {}", new Object[] { this.spellId, this.targetUUIDs.size(), this.targetUUIDs });
        buf.writeUtf(this.spellId);
        buf.writeInt(this.targetUUIDs.size());
        this.targetUUIDs.forEach(buf::m_130077_);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.setTargetingData(new ClientSpellTargetingData(this.spellId, this.targetUUIDs)));
        return true;
    }
}