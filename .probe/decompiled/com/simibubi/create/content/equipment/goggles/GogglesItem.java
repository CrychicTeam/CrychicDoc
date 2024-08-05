package com.simibubi.create.content.equipment.goggles;

import com.simibubi.create.AllItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class GogglesItem extends Item {

    private static final List<Predicate<Player>> IS_WEARING_PREDICATES = new ArrayList();

    public GogglesItem(Item.Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        EquipmentSlot equipmentslottype = Mob.m_147233_(itemstack);
        ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
        }
    }

    public static boolean isWearingGoggles(Player player) {
        for (Predicate<Player> predicate : IS_WEARING_PREDICATES) {
            if (predicate.test(player)) {
                return true;
            }
        }
        return false;
    }

    public static void addIsWearingPredicate(Predicate<Player> predicate) {
        IS_WEARING_PREDICATES.add(predicate);
    }

    static {
        addIsWearingPredicate(player -> AllItems.GOGGLES.isIn(player.getItemBySlot(EquipmentSlot.HEAD)));
    }
}