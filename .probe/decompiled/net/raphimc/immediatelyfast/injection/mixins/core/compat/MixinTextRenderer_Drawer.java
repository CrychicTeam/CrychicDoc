package net.raphimc.immediatelyfast.injection.mixins.core.compat;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Font.StringRenderOutput.class })
public abstract class MixinTextRenderer_Drawer {

    @Shadow
    @Final
    MultiBufferSource bufferSource;

    @Redirect(method = { "accept" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/Glyph;getAdvance(Z)F"))
    private float fixNegativeAdvanceGlyphs(GlyphInfo instance, boolean bold) {
        float advance = instance.getAdvance(bold);
        if (advance < 0.0F && !ImmediatelyFast.config.experimental_disable_resource_pack_conflict_handling && this.bufferSource instanceof MultiBufferSource.BufferSource) {
            ((MultiBufferSource.BufferSource) this.bufferSource).endLastBatch();
        }
        return advance;
    }
}