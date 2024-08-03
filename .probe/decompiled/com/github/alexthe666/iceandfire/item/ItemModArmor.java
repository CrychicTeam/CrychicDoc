package com.github.alexthe666.iceandfire.item;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemModArmor extends ArmorItem {

    public ItemModArmor(ArmorMaterial material, ArmorItem.Type slot) {
        super(material, slot, new Item.Properties());
    }

    @NotNull
    @Override
    public String getDescriptionId(@NotNull ItemStack stack) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                return "item.iceandfire.air_pods";
            }
        }
        return super.m_5671_(stack);
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.f_40379_ == IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_desert_layer_2" : "myrmex_desert_layer_1") + ".png";
        } else if (this.f_40379_ == IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_jungle_layer_2" : "myrmex_jungle_layer_1") + ".png";
        } else if (this.f_40379_ == IafItemRegistry.SHEEP_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "sheep_disguise_layer_2" : "sheep_disguise_layer_1") + ".png";
        } else {
            return this.f_40379_ == IafItemRegistry.EARPLUGS_ARMOR_MATERIAL ? "iceandfire:textures/models/armor/earplugs_layer_1.png" : null;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                tooltip.add(Component.translatable("item.iceandfire.air_pods.desc").withStyle(ChatFormatting.GREEN));
            }
        }
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }
}