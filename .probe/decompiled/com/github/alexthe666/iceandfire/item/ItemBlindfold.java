package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBlindfold extends ArmorItem {

    public ItemBlindfold() {
        super(IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties());
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        player.m_7292_(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0, false, false));
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/blindfold_layer_1.png";
    }
}