package io.redspace.ironsspellbooks.item.armor;

import java.util.Map;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

public interface IronsExtendedArmorMaterial extends ArmorMaterial {

    Map<Attribute, AttributeModifier> getAdditionalAttributes();
}