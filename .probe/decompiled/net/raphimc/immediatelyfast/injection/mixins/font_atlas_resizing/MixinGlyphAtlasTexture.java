package net.raphimc.immediatelyfast.injection.mixins.font_atlas_resizing;

import net.minecraft.client.gui.font.FontTexture;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({ FontTexture.class })
public abstract class MixinGlyphAtlasTexture {

    @ModifyConstant(method = { "*" }, constant = { @Constant(intValue = 256) })
    private int modifyGlyphAtlasTextureSize(int original) {
        return ImmediatelyFast.runtimeConfig.font_atlas_resizing ? 2048 : 256;
    }

    @ModifyConstant(method = { "*" }, constant = { @Constant(floatValue = 256.0F) })
    private float modifyGlyphAtlasTextureSize(float original) {
        return ImmediatelyFast.runtimeConfig.font_atlas_resizing ? 2048.0F : 256.0F;
    }
}