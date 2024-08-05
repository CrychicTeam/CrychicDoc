package com.squoshi.irons_spells_js.item;

import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class StaffItemBuilderJS extends HandheldItemBuilder {

    public transient List<StaffItemBuilderJS.AttributeHolder> additionalAttributes = new ArrayList();

    public StaffItemBuilderJS(ResourceLocation i) {
        super(i, 3.0F, -2.4F);
    }

    public StaffItemBuilderJS addAdditionalAttribute(ISSKJSUtils.AttributeHolder attribute, String modifierName, double modifierAmount, AttributeModifier.Operation modifierOperation) {
        this.additionalAttributes.add(new StaffItemBuilderJS.AttributeHolder(attribute.getLocation(), new AttributeModifier(modifierName, modifierAmount, modifierOperation)));
        return this;
    }

    public StaffItem createObject() {
        Map<Attribute, AttributeModifier> map = new HashMap(Map.of());
        for (StaffItemBuilderJS.AttributeHolder holder : this.additionalAttributes) {
            Attribute attribute = (Attribute) Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(holder.attribute()));
            map.put(attribute, holder.modifier());
        }
        return new StaffItem(this.createItemProperties(), (double) this.attackDamageBaseline, (double) this.speedBaseline, map);
    }

    public static record AttributeHolder(ResourceLocation attribute, AttributeModifier modifier) {
    }
}