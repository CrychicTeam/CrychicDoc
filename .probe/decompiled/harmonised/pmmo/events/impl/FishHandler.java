package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.TagUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

public class FishHandler {

    public static void handle(ItemFishedEvent event) {
        Player player = event.getEntity();
        Core core = Core.get(player.m_9236_());
        if (!core.isActionPermitted(ReqType.TOOL, event.getDrops().get(0), player)) {
            event.setCanceled(true);
            Messenger.sendDenialMsg(ReqType.TOOL, player, event.getDrops().get(0).getDisplayName());
        } else {
            boolean serverSide = !player.m_9236_().isClientSide;
            CompoundTag eventHookOutput = new CompoundTag();
            if (serverSide) {
                eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.FISH, event, new CompoundTag());
                if (eventHookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    return;
                }
            }
            CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.FISH, player, eventHookOutput));
            if (serverSide) {
                Map<String, Long> xpAward = new HashMap();
                for (ItemStack stack : event.getDrops()) {
                    System.out.println(stack.toString());
                    core.getExperienceAwards(EventType.FISH, stack, event.getEntity(), perkOutput).forEach((skill, value) -> xpAward.merge(skill, value, Long::sum));
                }
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                core.awardXP(partyMembersInRange, xpAward);
            }
        }
    }
}