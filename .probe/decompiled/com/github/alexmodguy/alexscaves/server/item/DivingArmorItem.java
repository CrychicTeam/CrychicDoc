package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

public class DivingArmorItem extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B77"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E12"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B43F"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB111") };

    private Multimap<Attribute, AttributeModifier> divingArmorAttributes;

    public DivingArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type slot) {
        super(armorMaterial, slot, new Item.Properties());
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIERS[this.f_265916_.ordinal()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double) this.m_40404_(), AttributeModifier.Operation.ADDITION));
        if (slot == ArmorItem.Type.LEGGINGS) {
            builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, "Swim speed", 0.5, AttributeModifier.Operation.ADDITION));
        } else if (slot == ArmorItem.Type.CHESTPLATE) {
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double) armorMaterial.getToughness(), AttributeModifier.Operation.ADDITION));
        }
        if (this.f_40378_ > 0.0F) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double) this.f_40378_, AttributeModifier.Operation.ADDITION));
        }
        this.divingArmorAttributes = builder.build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getArmorProperties());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return (Multimap<Attribute, AttributeModifier>) (equipmentSlot == this.f_265916_.getSlot() ? this.divingArmorAttributes : ImmutableMultimap.of());
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS ? "alexscaves:textures/armor/diving_suit_1.png" : "alexscaves:textures/armor/diving_suit_0.png";
    }
}