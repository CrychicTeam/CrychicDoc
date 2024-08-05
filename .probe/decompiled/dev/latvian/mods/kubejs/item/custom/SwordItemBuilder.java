package dev.latvian.mods.kubejs.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class SwordItemBuilder extends HandheldItemBuilder {

    public SwordItemBuilder(ResourceLocation i) {
        super(i, 3.0F, -2.4F);
    }

    public Item createObject() {
        return new SwordItem(this.toolTier, (int) this.attackDamageBaseline, this.speedBaseline, this.createItemProperties()) {

            private boolean modified = false;

            {
                this.f_43267_ = ArrayListMultimap.create(this.f_43267_);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!this.modified) {
                    this.modified = true;
                    SwordItemBuilder.this.attributes.forEach((r, m) -> this.f_43267_.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                }
                return super.getDefaultAttributeModifiers(equipmentSlot);
            }
        };
    }
}