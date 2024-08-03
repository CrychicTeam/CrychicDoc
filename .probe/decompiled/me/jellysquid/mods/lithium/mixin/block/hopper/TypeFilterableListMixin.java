package me.jellysquid.mods.lithium.mixin.block.hopper;

import net.minecraft.util.ClassInstanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { ClassInstanceMultiMap.class }, priority = 1010)
public class TypeFilterableListMixin<T> {

    @Redirect(method = { "getAllOfType(Ljava/lang/Class;)Ljava/util/Collection;" }, at = @At(value = "INVOKE", target = "Ljava/lang/Class;isAssignableFrom(Ljava/lang/Class;)Z", remap = false), require = 0)
    private boolean isAlwaysAssignable(Class<?> aClass, Class<?> cls) {
        return true;
    }
}