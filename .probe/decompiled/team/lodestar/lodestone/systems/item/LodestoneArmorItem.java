package team.lodestar.lodestone.systems.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LodestoneArmorItem extends ArmorItem {

    protected Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Item.Properties builder) {
        super(materialIn, type, builder);
    }

    public abstract Multimap<Attribute, AttributeModifier> createExtraAttributes(ArmorItem.Type var1);

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        if (this.attributes == null) {
            Builder<Attribute, AttributeModifier> attributeBuilder = new Builder();
            attributeBuilder.putAll(this.f_40383_);
            attributeBuilder.putAll(this.createExtraAttributes(this.f_265916_));
            this.attributes = attributeBuilder.build();
        }
        return (Multimap<Attribute, AttributeModifier>) (equipmentSlot == this.f_265916_.getSlot() ? this.attributes : ImmutableMultimap.of());
    }

    public String getTexture() {
        return null;
    }

    public String getTextureLocation() {
        return null;
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.getTextureLocation() + this.getTexture() + ".png";
    }
}