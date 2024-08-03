package com.blamejared.controlling.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ InputConstants.Key.class })
public interface AccessInputConstantsKey {

    @Accessor("NAME_MAP")
    static Map<String, InputConstants.Key> controlling$getNAME_MAP() {
        throw new AssertionError();
    }
}