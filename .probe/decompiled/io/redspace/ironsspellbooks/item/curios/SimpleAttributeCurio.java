package io.redspace.ironsspellbooks.item.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SimpleAttributeCurio extends CurioBaseItem {

    private final AttributeModifier attributeModifier;

    Multimap<Attribute, AttributeModifier> attributeMap;

    public SimpleAttributeCurio(Item.Properties properties, Attribute attribute, AttributeModifier attributeModifier) {
        super(properties);
        this.attributeModifier = attributeModifier;
        this.attributeMap = HashMultimap.create();
        this.attributeMap.put(attribute, this.attributeModifier);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Builder<Attribute, AttributeModifier> attributeBuilder = new Builder();
        for (Attribute attribute : this.attributeMap.keySet()) {
            attributeBuilder.put(attribute, new AttributeModifier(uuid, this.attributeModifier.getName(), this.attributeModifier.getAmount(), this.attributeModifier.getOperation()));
        }
        return attributeBuilder.build();
    }
}