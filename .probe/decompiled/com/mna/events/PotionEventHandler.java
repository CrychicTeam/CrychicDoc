package com.mna.events;

import com.mna.ManaAndArtifice;
import com.mna.effects.EffectInit;
import com.mna.effects.harmful.EffectDisjunction;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class PotionEventHandler {

    @SubscribeEvent
    public static void onPotionRemoved(MobEffectEvent.Remove event) {
        handlePotionRemoved(event.getEffect(), event.getEntity());
        if (event.getEffect() == EffectInit.REDUCE.get()) {
            event.getEntity().m_6210_();
        }
    }

    @SubscribeEvent
    public static void onPotionExpired(MobEffectEvent.Expired event) {
        handlePotionRemoved(event.getEffectInstance().getEffect(), event.getEntity());
        if (event.getEffectInstance().getEffect() == EffectInit.REDUCE.get()) {
            event.getEntity().m_6210_();
        }
    }

    private static void handlePotionRemoved(MobEffect effect, LivingEntity entity) {
        if (effect == EffectInit.BIND_WOUNDS.get() && entity == ManaAndArtifice.instance.proxy.getClientPlayer() && entity.getHealth() < entity.getMaxHealth() && entity.m_9236_().isClientSide()) {
            DidYouKnowHelper.CheckAndShowDidYouKnow(ManaAndArtifice.instance.proxy.getClientPlayer(), "helptip.mna.bind_wounds");
        }
        if (effect == EffectInit.MIST_FORM.get()) {
            if (entity instanceof Player player) {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                player.getAbilities().flying = false;
                player.f_19794_ = false;
                player.setForcedPose(null);
                EntityUtil.removeInvisibility(entity);
                player.m_6210_();
                player.getPersistentData().putInt("mna_remove_flight", 10);
            }
        } else if (effect != EffectInit.MIND_VISION.get() && effect != EffectInit.POSSESSION.get()) {
            if (effect == EffectInit.LEVITATION.get()) {
                if (entity instanceof Player) {
                    ((Player) entity).getPersistentData().putInt("mna_remove_flight", 10);
                }
            } else if (effect == EffectInit.TRUE_INVISIBILITY.get()) {
                entity.getPersistentData().putInt("mna_remove_ginvis", 10);
            } else if (effect == EffectInit.CAMOUFLAGE.get()) {
                entity.getPersistentData().remove("mna:camoflage_percent");
                entity.getPersistentData().putInt("mna_remove_ginvis", 10);
            }
        } else if (entity instanceof ServerPlayer) {
            ServerMessageDispatcher.sendPlayerMindVisionMessage((ServerPlayer) entity, null);
            entity.getPersistentData().remove("posessed_entity_id");
        }
        if (effect instanceof EffectWithCustomClientParticles) {
            ((EffectWithCustomClientParticles) effect).setFlags(entity, false);
        }
    }

    @SubscribeEvent
    public static void onPotionAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == EffectInit.MIST_FORM.get() && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
            player.getAbilities().flying = true;
            player.f_19794_ = true;
            player.setForcedPose(Pose.FALL_FLYING);
            player.m_6842_(true);
            player.m_6210_();
        }
        if (event.getEffectInstance().getEffect() == EffectInit.REDUCE.get()) {
            event.getEntity().m_6210_();
        }
        if (event.getEffectInstance().getEffect() instanceof EffectWithCustomClientParticles) {
            ((EffectWithCustomClientParticles) event.getEffectInstance().getEffect()).setFlags(event.getEntity(), true);
        }
    }

    @SubscribeEvent
    public static void onLivingChangedEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.items.size(); i++) {
                if (i != inventory.selected) {
                    EffectDisjunction.RemoveDisjunction(inventory.getItem(i));
                }
            }
            if (player.containerMenu != null) {
                EffectDisjunction.RemoveDisjunction(player.containerMenu.getCarried());
            }
            EffectDisjunction.RemoveDisjunction(event.getFrom());
            if (event.getEntity().hasEffect(EffectInit.DISJUNCTION.get())) {
                EffectDisjunction.ApplyDisjunction(event.getTo(), event.getEntity().getEffect(EffectInit.DISJUNCTION.get()).getAmplifier());
            }
        }
    }
}