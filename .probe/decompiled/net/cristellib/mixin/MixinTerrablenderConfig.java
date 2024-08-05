package net.cristellib.mixin;

import net.cristellib.util.TerrablenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import terrablender.config.Config;
import terrablender.config.TerraBlenderConfig;

@Mixin(value = { TerraBlenderConfig.class }, remap = false)
public class MixinTerrablenderConfig {

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lterrablender/config/Config;addNumber(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;)Ljava/lang/Number;"))
    private <T extends Number & Comparable<T>> Number create(Config instance, String comment, String key, T defaultValue, T min, T max) {
        return (Number) (TerrablenderUtil.mixinEnabled() && key.equals("vanilla_overworld_region_weight") && defaultValue instanceof Integer ? 0 : instance.addNumber(comment, key, defaultValue, min, max));
    }
}