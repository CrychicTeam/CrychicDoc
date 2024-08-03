package dev.latvian.mods.kubejs.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;

public class PickaxeItemBuilder extends HandheldItemBuilder {

    public PickaxeItemBuilder(ResourceLocation i) {
        super(i, 1.0F, -2.8F);
    }

    public Item createObject() {
        return new PickaxeItem(this.toolTier, (int) this.attackDamageBaseline, this.speedBaseline, this.createItemProperties()) {

            private boolean modified = false;

            {
                this.f_40982_ = ArrayListMultimap.create(this.f_40982_);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!this.modified) {
                    this.modified = true;
                    PickaxeItemBuilder.this.attributes.forEach((r, m) -> this.f_40982_.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                }
                return super.m_7167_(equipmentSlot);
            }
        };
    }
}