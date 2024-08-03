package org.embeddedt.modernfix.forge.mixin.perf.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RecipesEventJS.class })
@RequiresMod("kubejs")
public class RecipeEventJSMixin {

    @Inject(method = { "post" }, at = { @At("RETURN") }, remap = false)
    private void clearRecipeLists(CallbackInfo ci) {
        ModernFix.LOGGER.info("Clearing KubeJS recipe lists...");
        Field[] fields = RecipesEventJS.class.getDeclaredFields();
        for (Field f : fields) {
            try {
                if (!Modifier.isStatic(f.getModifiers()) && (Collection.class.isAssignableFrom(f.getType()) || Map.class.isAssignableFrom(f.getType()))) {
                    f.setAccessible(true);
                    Object collection = f.get(this);
                    int size;
                    if (collection instanceof Map) {
                        size = ((Map) collection).size();
                        ((Map) collection).clear();
                    } else {
                        if (!(collection instanceof Collection)) {
                            throw new IllegalStateException();
                        }
                        size = ((Collection) collection).size();
                        ((Collection) collection).clear();
                    }
                    ModernFix.LOGGER.debug("Cleared {} with {} entries", f.getName(), size);
                }
            } catch (ReflectiveOperationException | RuntimeException var9) {
                ModernFix.LOGGER.debug("Uh oh, couldn't clear field", var9);
            }
        }
    }
}