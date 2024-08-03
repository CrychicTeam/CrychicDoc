package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;

public class TradeHandler {

    public static void handle(TradeWithVillagerEvent event) {
        ItemStack tradeA = event.getMerchantOffer().getCostA();
        ItemStack tradeB = event.getMerchantOffer().getCostB();
        ItemStack result = event.getMerchantOffer().getResult();
        Core core = Core.get(event.getEntity().m_9236_());
        CompoundTag eventHookOutput = new CompoundTag();
        boolean serverSide = !event.getEntity().m_9236_().isClientSide;
        if (serverSide) {
            eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.GIVEN_AS_TRADE, event, new CompoundTag());
            eventHookOutput = TagUtils.mergeTags(eventHookOutput, core.getEventTriggerRegistry().executeEventListeners(EventType.RECEIVED_AS_TRADE, event, new CompoundTag()));
        }
        CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.GIVEN_AS_TRADE, event.getEntity(), eventHookOutput));
        perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.RECEIVED_AS_TRADE, event.getEntity(), perkOutput));
        if (serverSide) {
            Map<String, Long> xpAward = (Map<String, Long>) core.getExperienceAwards(EventType.GIVEN_AS_TRADE, tradeA, event.getEntity(), perkOutput).entrySet().stream().map(entry -> {
                entry.setValue((Long) entry.getValue() * (long) tradeA.getCount());
                return entry;
            }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            core.getExperienceAwards(EventType.GIVEN_AS_TRADE, tradeB, event.getEntity(), perkOutput).forEach((skill, value) -> xpAward.merge(skill, value * (long) tradeB.getCount(), Long::sum));
            core.getExperienceAwards(EventType.RECEIVED_AS_TRADE, result, event.getEntity(), perkOutput).forEach((skill, value) -> xpAward.merge(skill, value * (long) result.getCount(), Long::sum));
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getEntity());
            core.awardXP(partyMembersInRange, xpAward);
        }
    }
}