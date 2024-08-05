package dev.xkmc.l2weaponry.events;

import dev.xkmc.l2complements.events.ItemUseEventHandler;
import dev.xkmc.l2weaponry.content.item.base.DoubleWieldItem;
import dev.xkmc.l2weaponry.content.item.base.WeaponItem;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class LWClickListener implements ItemUseEventHandler.ItemClickHandler {

    @Override
    public boolean predicate(ItemStack itemStack, Class<? extends PlayerEvent> aClass, PlayerEvent playerEvent) {
        return itemStack.getItem() instanceof WeaponItem;
    }

    @Override
    public void onPlayerLeftClickEmpty(ItemStack stack, PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getLevel().isClientSide() && stack.getItem() instanceof DoubleWieldItem item) {
            doubleWeldSwing(event.getEntity(), item, stack);
        }
    }

    @Override
    public void onPlayerLeftClickBlock(ItemStack stack, PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide() && stack.getItem() instanceof DoubleWieldItem item) {
            doubleWeldSwing(event.getEntity(), item, stack);
        }
    }

    private static void doubleWeldSwing(Player player, DoubleWieldItem item, ItemStack stack) {
        if (stack.getEnchantmentLevel((Enchantment) LWEnchantments.GHOST_SLASH.get()) > 0 && (double) player.getAttackStrengthScale(0.0F) >= 0.9) {
            item.accumulateDamage(stack, player);
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, e -> e.m_21190_(InteractionHand.MAIN_HAND));
            }
        }
    }
}