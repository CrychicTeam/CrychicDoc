package net.blay09.mods.defaultoptions.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ KeyMapping.class })
public interface KeyMappingAccessor {

    @Accessor
    @Mutable
    void setDefaultKey(InputConstants.Key var1);
}