package com.mna.events;

import com.mna.effects.interfaces.IInputDisable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class InputDisabler {

    public static int getDisableInputMask(LivingEntity entity) {
        int mask = 0;
        for (MobEffectInstance effect : entity.getActiveEffects()) {
            if (effect.getEffect() instanceof IInputDisable) {
                mask |= ((IInputDisable) effect.getEffect()).getDisableMask();
            }
        }
        return mask;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        int mask = getDisableInputMask(event.getEntity());
        if ((mask & IInputDisable.InputMask.LEFT_CLICK.mask()) != 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        int mask = getDisableInputMask(event.getEntity());
        if ((mask & IInputDisable.InputMask.RIGHT_CLICK.mask()) != 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        int mask = getDisableInputMask(event.getEntity());
        if ((mask & IInputDisable.InputMask.RIGHT_CLICK.mask()) != 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        int mask = getDisableInputMask(event.getEntity());
        if ((mask & IInputDisable.InputMask.RIGHT_CLICK.mask()) != 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        int mask = getDisableInputMask(event.getEntity());
        if ((mask & IInputDisable.InputMask.RIGHT_CLICK.mask()) != 0) {
            event.setCanceled(true);
        }
    }
}