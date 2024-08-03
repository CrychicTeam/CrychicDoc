package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface Equipable extends Vanishable {

    EquipmentSlot getEquipmentSlot();

    default SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    default InteractionResultHolder<ItemStack> swapWithEquipmentSlot(Item item0, Level level1, Player player2, InteractionHand interactionHand3) {
        ItemStack $$4 = player2.m_21120_(interactionHand3);
        EquipmentSlot $$5 = Mob.m_147233_($$4);
        ItemStack $$6 = player2.getItemBySlot($$5);
        if (!EnchantmentHelper.hasBindingCurse($$6) && !ItemStack.matches($$4, $$6)) {
            if (!level1.isClientSide()) {
                player2.awardStat(Stats.ITEM_USED.get(item0));
            }
            ItemStack $$7 = $$6.isEmpty() ? $$4 : $$6.copyAndClear();
            ItemStack $$8 = $$4.copyAndClear();
            player2.setItemSlot($$5, $$8);
            return InteractionResultHolder.sidedSuccess($$7, level1.isClientSide());
        } else {
            return InteractionResultHolder.fail($$4);
        }
    }

    @Nullable
    static Equipable get(ItemStack itemStack0) {
        Item $$3 = itemStack0.getItem();
        if ($$3 instanceof Equipable) {
            return (Equipable) $$3;
        } else {
            if (itemStack0.getItem() instanceof BlockItem $$2) {
                Block var6 = $$2.getBlock();
                if (var6 instanceof Equipable) {
                    return (Equipable) var6;
                }
            }
            return null;
        }
    }
}