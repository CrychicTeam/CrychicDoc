package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Attribute.class })
public class AttributeMixin implements IFormattableAttribute {
}