package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DivingBootsItem extends BaseArmorItem {

    public static final EquipmentSlot SLOT = EquipmentSlot.FEET;

    public static final ArmorItem.Type TYPE = ArmorItem.Type.BOOTS;

    public DivingBootsItem(ArmorMaterial material, Item.Properties properties, ResourceLocation textureLoc) {
        super(material, TYPE, properties, textureLoc);
    }

    public static boolean isWornBy(Entity entity) {
        return !getWornItem(entity).isEmpty();
    }

    public static ItemStack getWornItem(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack stack = livingEntity.getItemBySlot(SLOT);
            return !(stack.getItem() instanceof DivingBootsItem) ? ItemStack.EMPTY : stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @SubscribeEvent
    public static void accellerateDescentUnderwater(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (affects(entity)) {
            Vec3 motion = entity.m_20184_();
            boolean isJumping = entity.jumping;
            entity.m_6853_(entity.m_20096_() || entity.f_19863_);
            if (isJumping && entity.m_20096_()) {
                motion = motion.add(0.0, 0.5, 0.0);
                entity.m_6853_(false);
            } else {
                motion = motion.add(0.0, -0.05F, 0.0);
            }
            float multiplier = 1.3F;
            if (motion.multiply(1.0, 0.0, 1.0).length() < 0.145F && (entity.zza > 0.0F || entity.xxa != 0.0F) && !entity.m_6144_()) {
                motion = motion.multiply((double) multiplier, 1.0, (double) multiplier);
            }
            entity.m_20256_(motion);
        }
    }

    protected static boolean affects(LivingEntity entity) {
        if (!isWornBy(entity)) {
            entity.getPersistentData().remove("HeavyBoots");
            return false;
        } else {
            NBTHelper.putMarker(entity.getPersistentData(), "HeavyBoots");
            if (!entity.m_20069_()) {
                return false;
            } else if (entity.m_20089_() == Pose.SWIMMING) {
                return false;
            } else {
                if (entity instanceof Player playerEntity && playerEntity.getAbilities().flying) {
                    return false;
                }
                return true;
            }
        }
    }

    public static Vec3 getMovementMultiplier(LivingEntity entity) {
        double yMotion = entity.m_20184_().y;
        double vMultiplier = yMotion < 0.0 ? Math.max(0.0, 2.5 - Math.abs(yMotion) * 2.0) : 1.0;
        if (entity.m_20096_()) {
            entity.getPersistentData().putBoolean("LavaGrounded", true);
            double hMultiplier = entity.m_20142_() ? 1.85 : 1.75;
            return new Vec3(hMultiplier, vMultiplier, hMultiplier);
        } else {
            if (entity.jumping && entity.getPersistentData().contains("LavaGrounded")) {
                boolean eyeInFluid = entity.m_204029_(FluidTags.LAVA);
                vMultiplier = yMotion == 0.0 ? 0.0 : (eyeInFluid ? 1.0 : 0.5) / yMotion;
            } else if (yMotion > 0.0) {
                vMultiplier = 1.3;
            }
            entity.getPersistentData().remove("LavaGrounded");
            return new Vec3(1.75, vMultiplier, 1.75);
        }
    }
}