package com.mna.api.entities.construct;

import com.mna.api.effects.MAEffects;
import com.mna.api.entities.DamageHelper;
import com.mna.api.events.construct.ConstructSprayEffectEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class DefaultFluidEventHandlers {

    @SubscribeEvent
    public static void onConstructSprayTarget(ConstructSprayEffectEvent event) {
        if (event.getFluid().isSame(Fluids.LAVA)) {
            event.getTarget().hurt(DamageHelper.createSourcedType(DamageTypes.LAVA, event.getConstruct().m_9236_().registryAccess(), event.getConstruct()), 4.0F);
            event.getTarget().m_20254_(15);
        } else if (event.getFluid().isSame(Fluids.WATER)) {
            event.getTarget().m_20095_();
            if (!event.getTarget().hasEffect(MAEffects.SOAKED)) {
                event.getTarget().addEffect(new MobEffectInstance(MAEffects.SOAKED, 200));
            }
            if (!event.isTargetFriendly()) {
                event.getTarget().hurt(DamageHelper.createSourcedType(DamageHelper.FROST, event.getConstruct().m_9236_().registryAccess(), event.getConstruct()), 2.0F);
            }
            event.getTarget().knockback(0.5, event.getConstruct().m_20185_() - event.getTarget().m_20185_(), event.getConstruct().m_20189_() - event.getTarget().m_20189_());
        } else if (event.getFluid().isSame(ForgeMod.MILK.get())) {
            event.getTarget().curePotionEffects(new ItemStack(Items.MILK_BUCKET));
        }
    }
}