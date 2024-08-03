package net.raphimc.immediatelyfast.injection.mixins.core.compat;

import com.mojang.blaze3d.font.GlyphProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.compat.CoreShaderBlacklist;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameRenderer.class })
public abstract class MixinGameRenderer {

    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Final
    private Map<String, ShaderInstance> shaders;

    @Inject(method = { "loadPrograms" }, at = { @At("RETURN") })
    private void checkForCoreShaderModifications(ResourceProvider factory, CallbackInfo ci) {
        boolean modified = false;
        for (Entry<String, ShaderInstance> shaderProgramEntry : this.shaders.entrySet()) {
            if (CoreShaderBlacklist.isBlacklisted((String) shaderProgramEntry.getKey())) {
                ResourceLocation vertexIdentifier = new ResourceLocation("shaders/core/" + ((ShaderInstance) shaderProgramEntry.getValue()).getVertexProgram().getName() + ".vsh");
                Resource resource = (Resource) factory.getResource(vertexIdentifier).orElse(null);
                if (resource != null && !resource.source().equals(this.minecraft.getVanillaPackResources())) {
                    modified = true;
                    break;
                }
            }
        }
        if (modified && !ImmediatelyFast.config.experimental_disable_resource_pack_conflict_handling) {
            ImmediatelyFast.LOGGER.warn("Core shader modifications detected. Temporarily disabling some parts of ImmediatelyFast.");
            if (ImmediatelyFast.runtimeConfig.font_atlas_resizing) {
                ImmediatelyFast.runtimeConfig.font_atlas_resizing = false;
                this.immediatelyFast$reloadFontStorages();
            }
            ImmediatelyFast.runtimeConfig.hud_batching = false;
        } else {
            if (!ImmediatelyFast.runtimeConfig.font_atlas_resizing && ImmediatelyFast.config.font_atlas_resizing) {
                ImmediatelyFast.runtimeConfig.font_atlas_resizing = true;
                this.immediatelyFast$reloadFontStorages();
            }
            ImmediatelyFast.runtimeConfig.hud_batching = ImmediatelyFast.config.hud_batching;
        }
    }

    @Unique
    private void immediatelyFast$reloadFontStorages() {
        for (FontSet storage : Minecraft.getInstance().fontManager.fontSets.values()) {
            List<GlyphProvider> fonts = new ArrayList(storage.providers);
            storage.providers.clear();
            storage.reload(fonts);
        }
    }
}