package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CraftHandler {

    public static void handle(PlayerEvent.ItemCraftedEvent event) {
        Core core = Core.get(event.getEntity().m_9236_());
        CompoundTag eventHookOutput = new CompoundTag();
        boolean serverSide = !event.getEntity().m_9236_().isClientSide;
        if (serverSide) {
            eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.CRAFT, event, new CompoundTag());
        }
        CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.CRAFT, event.getEntity(), eventHookOutput));
        if (serverSide) {
            Map<String, Long> xpAward = core.getExperienceAwards(EventType.CRAFT, event.getCrafting(), event.getEntity(), perkOutput);
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getEntity());
            core.awardXP(partyMembersInRange, xpAward);
        }
    }
}