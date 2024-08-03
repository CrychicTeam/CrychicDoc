package net.raphimc.immediatelyfast.injection.mixins.fast_text_lookup;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { FontManager.class }, priority = 500)
public abstract class MixinFontManager {

    @Shadow
    @Final
    private Map<ResourceLocation, FontSet> fontSets;

    @Shadow
    private Map<ResourceLocation, ResourceLocation> renames;

    @Unique
    private final Map<ResourceLocation, FontSet> immediatelyFast$overriddenFontStorages = new Object2ObjectOpenHashMap();

    @Unique
    private FontSet immediatelyFast$defaultFontStorage;

    @Unique
    private FontSet immediatelyFast$unicodeFontStorage;

    @Shadow
    protected abstract FontSet m_283955_(ResourceLocation var1);

    @Inject(method = { "reload(Lnet/minecraft/client/font/FontManager$ProviderIndex;Lnet/minecraft/util/profiler/Profiler;)V" }, at = { @At("RETURN") })
    private void rebuildOverriddenFontStoragesOnReload(CallbackInfo ci) {
        this.immediatelyFast$rebuildOverriddenFontStorages();
    }

    @Inject(method = { "setIdOverrides" }, at = { @At("RETURN") })
    private void rebuildOverriddenFontStoragesOnChange(CallbackInfo ci) {
        this.immediatelyFast$rebuildOverriddenFontStorages();
    }

    @ModifyArg(method = { "createTextRenderer", "createAdvanceValidatingTextRenderer" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;<init>(Ljava/util/function/Function;Z)V"))
    private Function<ResourceLocation, FontSet> overrideFontStorage(Function<ResourceLocation, FontSet> original) {
        return id -> {
            if (Minecraft.DEFAULT_FONT.equals(id) && this.immediatelyFast$defaultFontStorage != null) {
                return this.immediatelyFast$defaultFontStorage;
            } else if (Minecraft.UNIFORM_FONT.equals(id) && this.immediatelyFast$unicodeFontStorage != null) {
                return this.immediatelyFast$unicodeFontStorage;
            } else {
                FontSet storage = (FontSet) this.immediatelyFast$overriddenFontStorages.get(id);
                return storage != null ? storage : (FontSet) original.apply(id);
            }
        };
    }

    @Unique
    private void immediatelyFast$rebuildOverriddenFontStorages() {
        this.immediatelyFast$overriddenFontStorages.clear();
        this.immediatelyFast$overriddenFontStorages.putAll(this.fontSets);
        for (ResourceLocation key : this.renames.keySet()) {
            this.immediatelyFast$overriddenFontStorages.put(key, this.m_283955_(key));
        }
        this.immediatelyFast$defaultFontStorage = (FontSet) this.immediatelyFast$overriddenFontStorages.get(Minecraft.DEFAULT_FONT);
        this.immediatelyFast$unicodeFontStorage = (FontSet) this.immediatelyFast$overriddenFontStorages.get(Minecraft.UNIFORM_FONT);
    }
}