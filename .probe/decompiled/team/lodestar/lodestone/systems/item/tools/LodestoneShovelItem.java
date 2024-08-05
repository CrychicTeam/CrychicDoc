package team.lodestar.lodestone.systems.item.tools;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class LodestoneShovelItem extends ShovelItem {

    private Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneShovelItem(Tier material, float damage, float speed, Item.Properties properties) {
        super(material, damage + 1.5F, speed - 3.0F, properties.durability(material.getUses()));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (this.attributes == null) {
            Builder<Attribute, AttributeModifier> attributeBuilder = new Builder();
            attributeBuilder.putAll(this.f_40982_);
            attributeBuilder.putAll(this.createExtraAttributes().build());
            this.attributes = attributeBuilder.build();
        }
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributes : super.m_7167_(equipmentSlot);
    }

    public Builder<Attribute, AttributeModifier> createExtraAttributes() {
        return new Builder();
    }
}