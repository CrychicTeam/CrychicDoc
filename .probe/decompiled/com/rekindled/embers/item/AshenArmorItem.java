package com.rekindled.embers.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.rekindled.embers.model.AshenArmorModel;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class AshenArmorItem extends ArmorItem {

    public AshenArmorItem(ArmorMaterial material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "embers:textures/models/armor/robe.png";
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
        super.getDefaultAttributeModifiers(slot);
        return (Multimap<Attribute, AttributeModifier>) (this.isBroken(stack) ? ImmutableMultimap.of() : modifiers);
    }

    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, Math.min(damage, this.getMaxDamage(stack) - 1));
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return this.isBroken(stack) ? 0 : amount;
    }

    public boolean isBroken(ItemStack armor) {
        return armor.getDamageValue() >= armor.getMaxDamage() - 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.m_7373_(stack, level, tooltip, isAdvanced);
        if (this.isBroken(stack)) {
            tooltip.add(Component.translatable("embers.tooltip.broken").withStyle(ChatFormatting.GRAY));
        }
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(AshenArmorModel.ARMOR_MODEL_GETTER);
    }
}