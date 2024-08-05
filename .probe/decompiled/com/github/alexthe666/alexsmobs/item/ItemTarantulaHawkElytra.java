package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemTarantulaHawkElytra extends ArmorItem {

    public ItemTarantulaHawkElytra(Item.Properties props, AMArmorMaterial mat) {
        super(mat, ArmorItem.Type.CHESTPLATE, props);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getArmorRenderProperties());
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        EquipmentSlot equipmentslottype = Mob.m_147233_(itemstack);
        ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ElytraItem.isFlyEnabled(stack);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.m_9236_().isClientSide && (flightTicks + 1) % 20 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
        }
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == AMItemRegistry.TARANTULA_HAWK_WING_FRAGMENT.get();
    }

    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "alexsmobs:textures/armor/tarantula_hawk_elytra.png";
    }
}