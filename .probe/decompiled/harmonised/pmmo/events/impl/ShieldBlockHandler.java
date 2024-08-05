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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class ShieldBlockHandler {

    public static void handle(ShieldBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamageSource().getEntity() != null) {
                Player player = (Player) event.getEntity();
                Core core = Core.get(player.m_9236_());
                ItemStack shield = player.m_21211_();
                Entity attacker = event.getDamageSource().getEntity();
                if (!core.isActionPermitted(ReqType.WEAPON, shield, player)) {
                    event.setCanceled(true);
                    Messenger.sendDenialMsg(ReqType.WEAPON, player, shield.getDisplayName());
                } else {
                    boolean serverSide = !player.m_9236_().isClientSide;
                    CompoundTag hookOutput = new CompoundTag();
                    if (serverSide) {
                        hookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.SHIELD_BLOCK, event, new CompoundTag());
                        if (hookOutput.getBoolean("is_cancelled")) {
                            event.setCanceled(true);
                            return;
                        }
                    }
                    hookOutput.putFloat("damageIn", event.getBlockedDamage());
                    hookOutput = TagUtils.mergeTags(hookOutput, core.getPerkRegistry().executePerk(EventType.SHIELD_BLOCK, player, hookOutput));
                    if (serverSide) {
                        Map<String, Long> xpAward = new HashMap();
                        core.getExperienceAwards(EventType.SHIELD_BLOCK, attacker, player, hookOutput).forEach((skill, value) -> xpAward.put(skill, (long) ((float) value.longValue() * event.getBlockedDamage())));
                        List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                        core.awardXP(partyMembersInRange, xpAward);
                    }
                }
            }
        }
    }
}