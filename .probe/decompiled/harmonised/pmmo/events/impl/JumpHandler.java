package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.TagUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;

public class JumpHandler {

    public static void handle(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            EventType type = EventType.JUMP;
            if (player.m_20142_()) {
                type = EventType.SPRINT_JUMP;
            } else if (player.m_6047_()) {
                type = EventType.CROUCH_JUMP;
            }
            Core core = Core.get(player.m_9236_());
            CompoundTag eventHookOutput = new CompoundTag();
            boolean serverSide = !player.m_9236_().isClientSide;
            if (serverSide) {
                eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(type, event, new CompoundTag());
                if (eventHookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    return;
                }
            }
            CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(type, player, eventHookOutput));
            if (serverSide) {
                double jumpXpBase = perkOutput.contains("jump_boost_output") ? Math.max(0.4, perkOutput.getDouble("jump_boost_output")) : player.m_20184_().y;
                Map<String, Long> xpAward = new HashMap();
                Map<String, Double> ratios = getRatioMap(type);
                ratios.keySet().forEach(skill -> {
                    Double xpValue = (Double) ratios.getOrDefault(skill, 2.5) * jumpXpBase * (Double) core.getConsolidatedModifierMap(player).getOrDefault(skill, 1.0);
                    xpAward.put(skill, xpValue.longValue());
                });
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                core.awardXP(partyMembersInRange, xpAward);
            }
        }
    }

    private static Map<String, Double> getRatioMap(EventType type) {
        return (Map<String, Double>) (type.equals(EventType.JUMP) ? Config.JUMP_XP.get() : (type.equals(EventType.SPRINT_JUMP) ? Config.SPRINT_JUMP_XP.get() : (type.equals(EventType.CROUCH_JUMP) ? Config.CROUCH_JUMP_XP.get() : new HashMap())));
    }
}