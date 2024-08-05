package dev.shadowsoffire.attributeslib.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ AttributeInstance.class })
public interface AttributeInstanceAccessor {

    @Accessor
    double getCachedValue();
}