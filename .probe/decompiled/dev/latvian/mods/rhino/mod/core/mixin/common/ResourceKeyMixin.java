package dev.latvian.mods.rhino.mod.core.mixin.common;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import dev.latvian.mods.rhino.util.SpecialEquality;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("rhino$")
@Mixin(value = { ResourceKey.class }, priority = 1001)
public abstract class ResourceKeyMixin implements SpecialEquality {

    @Shadow
    @Final
    private ResourceLocation location;

    public String rhino$getNamespace() {
        return this.location.getNamespace();
    }

    public String rhino$getPath() {
        return this.location.getPath();
    }

    @Override
    public boolean specialEquals(Object o, boolean shallow) {
        if (this == o) {
            return true;
        } else if (o instanceof ResourceKey) {
            return false;
        } else {
            return o instanceof ResourceLocation ? this.location.equals(o) : this.location.toString().equals(String.valueOf(o));
        }
    }
}