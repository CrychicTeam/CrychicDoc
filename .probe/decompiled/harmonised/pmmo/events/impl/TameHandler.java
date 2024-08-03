package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.AnimalTameEvent;

public class TameHandler {

    public static void handle(AnimalTameEvent event) {
        Core core = Core.get(event.getTamer().m_9236_());
        Player player = event.getTamer();
        Animal target = event.getAnimal();
        if (!core.isActionPermitted(ReqType.TAME, target, player)) {
            event.setCanceled(true);
            Messenger.sendDenialMsg(ReqType.TAME, player, target.m_7755_());
        } else {
            boolean serverSide = !player.m_9236_().isClientSide;
            CompoundTag hookOutput = new CompoundTag();
            if (serverSide) {
                hookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.TAMING, event, new CompoundTag());
                if (hookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    return;
                }
            }
            hookOutput.putUUID("tamed", event.getAnimal().m_20148_());
            hookOutput = TagUtils.mergeTags(hookOutput, core.getPerkRegistry().executePerk(EventType.TAMING, player, hookOutput));
            if (serverSide) {
                Map<String, Long> xpAward = core.getExperienceAwards(EventType.TAMING, target, player, hookOutput);
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                core.awardXP(partyMembersInRange, xpAward);
            }
        }
    }
}