package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ElytraItem extends Item implements Equipable {

    public ElytraItem(Item.Properties itemProperties0) {
        super(itemProperties0);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public static boolean isFlyEnabled(ItemStack itemStack0) {
        return itemStack0.getDamageValue() < itemStack0.getMaxDamage() - 1;
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack0, ItemStack itemStack1) {
        return itemStack1.is(Items.PHANTOM_MEMBRANE);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        return this.m_269277_(this, level0, player1, interactionHand2);
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_ELYTRA;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}