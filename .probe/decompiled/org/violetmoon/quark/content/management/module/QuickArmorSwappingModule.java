package org.violetmoon.quark.content.management.module;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "management")
public class QuickArmorSwappingModule extends ZetaModule {

    @Config
    public static boolean swapOffHand = true;

    @PlayEvent
    public void onEntityInteractSpecific(ZPlayerInteract.EntityInteractSpecific event) {
        Player player = event.getEntity();
        if (!player.isSpectator() && !player.getAbilities().instabuild && event.getTarget() instanceof ArmorStand armorStand) {
            if (player.m_6047_()) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide));
                this.swapSlot(player, armorStand, EquipmentSlot.HEAD);
                this.swapSlot(player, armorStand, EquipmentSlot.CHEST);
                this.swapSlot(player, armorStand, EquipmentSlot.LEGS);
                this.swapSlot(player, armorStand, EquipmentSlot.FEET);
                if (swapOffHand) {
                    this.swapSlot(player, armorStand, EquipmentSlot.OFFHAND);
                }
            }
        }
    }

    private void swapSlot(Player player, ArmorStand armorStand, EquipmentSlot slot) {
        ItemStack playerItem = player.getItemBySlot(slot);
        ItemStack armorStandItem = armorStand.getItemBySlot(slot);
        if (!EnchantmentHelper.hasBindingCurse(playerItem)) {
            ItemStack held = player.m_21120_(InteractionHand.MAIN_HAND);
            if (armorStandItem.isEmpty() && !held.isEmpty() && Player.m_147233_(held) == slot) {
                ItemStack copy = held.copy();
                player.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                armorStandItem = copy;
            }
            player.setItemSlot(slot, armorStandItem);
            armorStand.setItemSlot(slot, playerItem);
        }
    }
}