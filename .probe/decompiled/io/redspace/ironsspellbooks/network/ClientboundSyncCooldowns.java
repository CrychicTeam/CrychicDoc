package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncCooldowns {

    private final Map<String, CooldownInstance> spellCooldowns;

    public static String readSpellID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static CooldownInstance readCoolDownInstance(FriendlyByteBuf buffer) {
        int spellCooldown = buffer.readInt();
        int spellCooldownRemaining = buffer.readInt();
        return new CooldownInstance(spellCooldown, spellCooldownRemaining);
    }

    public static void writeSpellId(FriendlyByteBuf buf, String spellId) {
        buf.writeUtf(spellId);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, CooldownInstance cooldownInstance) {
        buf.writeInt(cooldownInstance.getSpellCooldown());
        buf.writeInt(cooldownInstance.getCooldownRemaining());
    }

    public ClientboundSyncCooldowns(Map<String, CooldownInstance> spellCooldowns) {
        this.spellCooldowns = spellCooldowns;
    }

    public ClientboundSyncCooldowns(FriendlyByteBuf buf) {
        this.spellCooldowns = buf.readMap(ClientboundSyncCooldowns::readSpellID, ClientboundSyncCooldowns::readCoolDownInstance);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.spellCooldowns, ClientboundSyncCooldowns::writeSpellId, ClientboundSyncCooldowns::writeCoolDownInstance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            PlayerCooldowns cooldowns = ClientMagicData.getCooldowns();
            cooldowns.clearCooldowns();
            this.spellCooldowns.forEach((k, v) -> cooldowns.addCooldown(k, v.getSpellCooldown(), v.getCooldownRemaining()));
            ClientMagicData.resetClientCastState(null);
        });
        return true;
    }
}