package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "crockpot")
public class CrockPotEffectsEvent {

    @SubscribeEvent
    public static void onLivingEntityAttacked(LivingHurtEvent event) {
        if (event.getEntity().m_20071_()) {
            DamageSource source = event.getSource();
            if ((source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) && source.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(CrockPotEffects.CHARGE.get())) {
                event.setAmount(event.getAmount() * 1.3F);
            }
        }
    }

    @SubscribeEvent
    public static void onWitherEffectApply(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == MobEffects.WITHER && event.getEntity().hasEffect(CrockPotEffects.WITHER_RESISTANCE.get())) {
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistanceEffectAdded(MobEffectEvent.Added event) {
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance().getEffect() == CrockPotEffects.WITHER_RESISTANCE.get() && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }

    @SubscribeEvent
    public static void onFoodRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.m_21023_(CrockPotEffects.GNAWS_GIFT.get()) && event.getItemStack().isEdible()) {
            player.m_6672_(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}