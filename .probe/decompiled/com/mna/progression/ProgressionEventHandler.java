package com.mna.progression;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.recipes.progression.ProgressionCondition;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class ProgressionEventHandler {

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        IPlayerProgression progression = (IPlayerProgression) event.getEntity().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression != null && progression.getTier() == 1 && progression.getTierProgress(event.getEntity().m_9236_()) >= 1.0F) {
            progression.setTier(progression.getTier() + 1, event.getEntity());
            event.getEntity().m_213846_(Component.translatable("mna:progresscondition.advanced", progression.getTier()));
        }
    }

    @SubscribeEvent
    public static void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Player player = event.getEntity();
        if (player != null) {
            if (player.m_9236_().isClientSide()) {
                confirmExistingAdvancements(event.getEntity());
            } else {
                IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (progression == null || progression.getTier() >= 5) {
                    return;
                }
                List<ProgressionCondition> conditions = ProgressionCondition.get(player.m_9236_(), progression.getTier(), progression.getCompletedProgressionSteps());
                boolean advanced = false;
                for (ProgressionCondition condition : conditions) {
                    if (condition.getAdvancementID().equals(event.getAdvancement().getId())) {
                        advanced = true;
                        progression.addTierProgressionComplete(condition.m_6423_());
                    }
                }
                if (advanced && progression.getTierProgress(player.m_9236_()) >= 1.0F) {
                    player.m_213846_(Component.translatable("mna:progresscondition.ready"));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().m_9236_().isClientSide()) {
            confirmExistingAdvancements(event.getEntity());
        }
    }

    public static void confirmExistingAdvancements(Player player) {
        IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression != null && progression.getTier() < 5) {
            List<ProgressionCondition> conditions = ProgressionCondition.get(player.m_9236_(), progression.getTier(), progression.getCompletedProgressionSteps());
            PlayerAdvancements advancements = ((ServerLevel) player.m_9236_()).getServer().getPlayerList().getPlayerAdvancements((ServerPlayer) player);
            ServerAdvancementManager advancementMgr = ((ServerLevel) player.m_9236_()).getServer().getAdvancements();
            for (ProgressionCondition condition : conditions) {
                for (Advancement adv : advancementMgr.getAllAdvancements()) {
                    if (advancements.getOrStartProgress(adv).isDone() && condition.getAdvancementID().equals(adv.getId())) {
                        progression.addTierProgressionComplete(condition.m_6423_());
                    }
                }
            }
        }
    }
}