package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class HazmatArmorItem extends ArmorItem {

    public HazmatArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type slot) {
        super(armorMaterial, slot, new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getArmorProperties());
    }

    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (stack.is(ACItemRegistry.HAZMAT_MASK.get()) && Math.cos((double) ((float) player.f_19797_ * 0.05F)) >= 0.9F) {
            Vec3 eyes = player.m_146892_();
            if (level.random.nextBoolean()) {
                Vec3 leftOffset = new Vec3(0.25, -0.3F, 0.25).xRot((float) Math.toRadians((double) (-player.m_146909_()))).yRot((float) Math.toRadians((double) (-player.m_6080_())));
                level.addParticle(ACParticleRegistry.HAZMAT_BREATHE.get(), eyes.x + leftOffset.x, eyes.y + leftOffset.y, eyes.z + leftOffset.z, (double) ((level.random.nextFloat() - 0.5F) * 0.1F), (double) ((level.random.nextFloat() - 0.5F) * 0.1F), (double) ((level.random.nextFloat() - 0.5F) * 0.1F));
            }
            if (level.random.nextBoolean()) {
                Vec3 rightOffset = new Vec3(-0.25, -0.3F, 0.25).xRot((float) Math.toRadians((double) (-player.m_146909_()))).yRot((float) Math.toRadians((double) (-player.m_6080_())));
                level.addParticle(ACParticleRegistry.HAZMAT_BREATHE.get(), eyes.x + rightOffset.x, eyes.y + rightOffset.y, eyes.z + rightOffset.z, (double) ((level.random.nextFloat() - 0.5F) * 0.1F), (double) ((level.random.nextFloat() - 0.5F) * 0.1F), (double) ((level.random.nextFloat() - 0.5F) * 0.1F));
            }
        }
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS ? "alexscaves:textures/armor/hazmat_suit_1.png" : "alexscaves:textures/armor/hazmat_suit_0.png";
    }

    public static int getWornAmount(LivingEntity entity) {
        int i = 0;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HAZMAT_MASK.get())) {
            i++;
        }
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.HAZMAT_CHESTPLATE.get())) {
            i++;
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).is(ACItemRegistry.HAZMAT_LEGGINGS.get())) {
            i++;
        }
        if (entity.getItemBySlot(EquipmentSlot.FEET).is(ACItemRegistry.HAZMAT_BOOTS.get())) {
            i++;
        }
        return i;
    }
}