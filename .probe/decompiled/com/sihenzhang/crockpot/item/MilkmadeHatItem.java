package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.MilkmadeHatCuriosCapabilityProvider;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

public class MilkmadeHatItem extends CrockPotBaseItem {

    public MilkmadeHatItem() {
        this(new Item.Properties().durability(180).setNoRepair());
    }

    protected MilkmadeHatItem(Item.Properties pProperties) {
        super(pProperties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        if (ModList.get().isLoaded("curios") && entity instanceof LivingEntity livingEntity && CuriosUtils.anyMatchInEquippedCurios(livingEntity, CrockPotItemTags.MILKMADE_HATS)) {
            return false;
        }
        return super.canEquip(stack, armorType, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.m_21120_(pUsedHand);
        if (ModList.get().isLoaded("curios") && CuriosUtils.anyMatchInEquippedCurios(pPlayer, CrockPotItemTags.MILKMADE_HATS)) {
            return InteractionResultHolder.fail(stack);
        } else {
            EquipmentSlot equipmentSlotForItem = LivingEntity.getEquipmentSlotForItem(stack);
            ItemStack stackBySlot = pPlayer.getItemBySlot(equipmentSlotForItem);
            if (stackBySlot.isEmpty()) {
                pPlayer.setItemSlot(equipmentSlotForItem, stack.copy());
                stack.setCount(0);
                return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this)) {
            stack.hurtAndBreak(1, player, e -> e.m_21166_(EquipmentSlot.HEAD));
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 100);
        }
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return (ICapabilityProvider) (ModList.get().isLoaded("curios") ? new MilkmadeHatCuriosCapabilityProvider(stack, nbt) : super.initCapabilities(stack, nbt));
    }
}