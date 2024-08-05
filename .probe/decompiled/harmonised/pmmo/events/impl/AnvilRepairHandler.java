package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;

public class AnvilRepairHandler {

    public static void handle(AnvilRepairEvent event) {
        Core core = Core.get(event.getEntity().m_9236_());
        CompoundTag eventHookOutput = new CompoundTag();
        boolean serverSide = !event.getEntity().m_9236_().isClientSide;
        EventType type = !isEnchantBook(event.getLeft()) && !isEnchantBook(event.getRight()) ? EventType.ANVIL_REPAIR : EventType.ENCHANT;
        if (serverSide) {
            eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(type, event, new CompoundTag());
        }
        CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(type, event.getEntity(), eventHookOutput));
        if (serverSide) {
            Map<String, Long> xpAward = core.getExperienceAwards(type, event.getOutput(), event.getEntity(), perkOutput);
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getEntity());
            core.awardXP(partyMembersInRange, xpAward);
        }
    }

    private static boolean isEnchantBook(ItemStack stack) {
        return stack.getItem() instanceof EnchantedBookItem;
    }
}