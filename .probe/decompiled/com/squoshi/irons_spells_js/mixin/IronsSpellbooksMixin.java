package com.squoshi.irons_spells_js.mixin;

import com.squoshi.irons_spells_js.IronsSpellsJSMod;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { IronsSpellbooks.class }, remap = false)
public class IronsSpellbooksMixin {

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/ModLoadingContext;registerConfig(Lnet/minecraftforge/fml/config/ModConfig$Type;Lnet/minecraftforge/fml/config/IConfigSpec;Ljava/lang/String;)V", ordinal = 1))
    private void kjs_irons_spells$cancelConfig(ModLoadingContext instance, Type type, IConfigSpec<?> spec, String fileName) {
        IronsSpellsJSMod.LOGGER.info("Postponing IronSpells server config...");
    }
}