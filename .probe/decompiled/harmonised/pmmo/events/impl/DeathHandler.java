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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class DeathHandler {

    public static void handle(LivingDeathEvent event) {
        if (event.getSource().getEntity() != null) {
            if (event.getSource().getEntity() instanceof Player) {
                LivingEntity target = event.getEntity();
                if (target == null) {
                    return;
                }
                Player player = (Player) event.getSource().getEntity();
                if (target.equals(player)) {
                    return;
                }
                Core core = Core.get(player.m_9236_());
                if (!core.isActionPermitted(ReqType.WEAPON, player.m_21205_(), player)) {
                    event.setCanceled(true);
                    Messenger.sendDenialMsg(ReqType.WEAPON, player, player.m_21205_().getDisplayName());
                    return;
                }
                if (!core.isActionPermitted(ReqType.KILL, target, player)) {
                    event.setCanceled(true);
                    Messenger.sendDenialMsg(ReqType.KILL, player, target.m_7755_());
                    return;
                }
                boolean serverSide = !player.m_9236_().isClientSide;
                CompoundTag hookOutput = new CompoundTag();
                if (serverSide) {
                    hookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.DEATH, event, new CompoundTag());
                    if (hookOutput.getBoolean("is_cancelled")) {
                        event.setCanceled(true);
                        return;
                    }
                }
                hookOutput = TagUtils.mergeTags(hookOutput, core.getPerkRegistry().executePerk(EventType.DEATH, player, hookOutput));
                if (serverSide) {
                    Map<String, Long> xpAward = core.getExperienceAwards(EventType.DEATH, target, player, hookOutput);
                    List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                    core.awardXP(partyMembersInRange, xpAward);
                }
            }
        }
    }
}