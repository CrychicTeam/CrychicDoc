package net.dnlayu.fastboot.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ SharedConstants.class })
public class SharedConstantsMixin {

    @Overwrite
    public static void enableDataFixerOptimizations() {
    }
}