package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;

public class PotionHandler {

    private static final String BREWED = "brewXpAwarded";

    public static void handle(PlayerBrewedPotionEvent event) {
        ItemStack stack = event.getStack();
        if (!Config.BREWING_TRACKED.get() || stack.getTag() == null || !stack.getTag().getBoolean("brewXpAwarded")) {
            Player player = event.getEntity();
            Core core = Core.get(player.m_9236_());
            boolean serverSide = !player.m_9236_().isClientSide;
            CompoundTag perkOutput = core.getPerkRegistry().executePerk(EventType.BREW, player, new CompoundTag());
            if (serverSide) {
                Map<String, Long> xpAward = core.getExperienceAwards(EventType.BREW, stack, event.getEntity(), perkOutput);
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getEntity());
                core.awardXP(partyMembersInRange, xpAward);
            }
            if (Config.BREWING_TRACKED.get() && stack.getTag() != null) {
                stack.getTag().putBoolean("brewXpAwarded", true);
            }
        }
    }
}