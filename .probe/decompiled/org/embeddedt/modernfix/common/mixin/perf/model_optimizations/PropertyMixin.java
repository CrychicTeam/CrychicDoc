package org.embeddedt.modernfix.common.mixin.perf.model_optimizations;

import net.minecraft.world.level.block.state.properties.Property;
import org.embeddedt.modernfix.dedup.IdentifierCaches;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Property.class })
public class PropertyMixin {

    @Shadow
    @Mutable
    @Final
    private String name;

    @Shadow
    private Integer hashCode;

    @Shadow
    @Final
    private Class clazz;

    @Redirect(method = { "<init>" }, at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/state/properties/Property;name:Ljava/lang/String;"))
    private void internName(Property instance, String name) {
        this.name = IdentifierCaches.PROPERTY.deduplicate(name);
    }

    @Overwrite(remap = false)
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else {
            return !(p_equals_1_ instanceof Property<?> property) ? false : this.clazz == property.getValueClass() && this.name == property.getName();
        }
    }
}