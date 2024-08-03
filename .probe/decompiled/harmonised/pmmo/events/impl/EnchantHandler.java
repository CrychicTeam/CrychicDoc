package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.events.EnchantEvent;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.TagBuilder;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class EnchantHandler {

    public static void handle(EnchantEvent event) {
        Core core = Core.get(event.getEntity().m_9236_());
        CompoundTag hookOutput = new CompoundTag();
        boolean serverSide = !event.getEntity().m_9236_().isClientSide;
        if (serverSide) {
            CompoundTag dataIn = TagBuilder.start().withString("stack", event.getItem().serializeNBT().m_7916_()).withString("player_id", event.getEntity().m_20148_().toString()).withInt("enchant_level", event.getEnchantment().level).withString("enchant_name", event.getEnchantment().enchantment.getDescriptionId()).build();
            hookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.ENCHANT, event, dataIn);
        }
        hookOutput = TagUtils.mergeTags(hookOutput, core.getPerkRegistry().executePerk(EventType.ENCHANT, event.getEntity(), hookOutput));
        if (serverSide) {
            double proportion = (double) event.getEnchantment().level / (double) event.getEnchantment().enchantment.getMaxLevel();
            Map<String, Long> xpAward = core.getExperienceAwards(EventType.ENCHANT, event.getItem(), event.getEntity(), hookOutput);
            Set<String> keys = xpAward.keySet();
            keys.forEach(skill -> xpAward.computeIfPresent(skill, (key, value) -> Double.valueOf((double) value.longValue() * proportion).longValue()));
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getEntity());
            core.awardXP(partyMembersInRange, xpAward);
        }
    }
}