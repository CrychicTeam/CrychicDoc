package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.AllTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class NetheriteDivingHandler {

    public static final String NETHERITE_DIVING_BITS_KEY = "CreateNetheriteDivingBits";

    public static final String FIRE_IMMUNE_KEY = "CreateFireImmune";

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        EquipmentSlot slot = event.getSlot();
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            LivingEntity entity = event.getEntity();
            ItemStack to = event.getTo();
            if (slot == EquipmentSlot.HEAD) {
                if (isNetheriteDivingHelmet(to)) {
                    setBit(entity, slot);
                } else {
                    clearBit(entity, slot);
                }
            } else if (slot == EquipmentSlot.CHEST) {
                if (isNetheriteBacktank(to) && BacktankUtil.hasAirRemaining(to)) {
                    setBit(entity, slot);
                } else {
                    clearBit(entity, slot);
                }
            } else if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                if (isNetheriteArmor(to)) {
                    setBit(entity, slot);
                } else {
                    clearBit(entity, slot);
                }
            }
        }
    }

    public static boolean isNetheriteDivingHelmet(ItemStack stack) {
        return stack.getItem() instanceof DivingHelmetItem && isNetheriteArmor(stack);
    }

    public static boolean isNetheriteBacktank(ItemStack stack) {
        return stack.is(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag) && isNetheriteArmor(stack);
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.NETHERITE) {
            return true;
        }
        return false;
    }

    public static void setBit(LivingEntity entity, EquipmentSlot slot) {
        CompoundTag nbt = entity.getPersistentData();
        byte bits = nbt.getByte("CreateNetheriteDivingBits");
        if ((bits & 15) != 15) {
            bits = (byte) (bits | 1 << slot.getIndex());
            nbt.putByte("CreateNetheriteDivingBits", bits);
            if ((bits & 15) == 15) {
                setFireImmune(entity, true);
            }
        }
    }

    public static void clearBit(LivingEntity entity, EquipmentSlot slot) {
        CompoundTag nbt = entity.getPersistentData();
        if (nbt.contains("CreateNetheriteDivingBits")) {
            byte bits = nbt.getByte("CreateNetheriteDivingBits");
            boolean prevFullSet = (bits & 15) == 15;
            bits = (byte) (bits & ~(1 << slot.getIndex()));
            nbt.putByte("CreateNetheriteDivingBits", bits);
            if (prevFullSet) {
                setFireImmune(entity, false);
            }
        }
    }

    public static void setFireImmune(LivingEntity entity, boolean fireImmune) {
        entity.getPersistentData().putBoolean("CreateFireImmune", fireImmune);
    }
}