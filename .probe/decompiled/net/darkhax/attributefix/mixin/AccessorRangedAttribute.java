package net.darkhax.attributefix.mixin;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ RangedAttribute.class })
public interface AccessorRangedAttribute {

    @Accessor("minValue")
    @Mutable
    void attributefix$setMinValue(double var1);

    @Accessor("maxValue")
    @Mutable
    void attributefix$setMaxValue(double var1);
}