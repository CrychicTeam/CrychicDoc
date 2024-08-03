package team.lodestar.lodestone.systems.item.tools;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class LodestoneSwordItem extends SwordItem {

    private Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneSwordItem(Tier material, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(material, attackDamage + 3, attackSpeed - 2.4F, properties.durability(material.getUses()));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (this.attributes == null) {
            Builder<Attribute, AttributeModifier> attributeBuilder = new Builder();
            attributeBuilder.putAll(this.f_43267_);
            attributeBuilder.putAll(this.createExtraAttributes().build());
            this.attributes = attributeBuilder.build();
        }
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public Builder<Attribute, AttributeModifier> createExtraAttributes() {
        return new Builder();
    }
}