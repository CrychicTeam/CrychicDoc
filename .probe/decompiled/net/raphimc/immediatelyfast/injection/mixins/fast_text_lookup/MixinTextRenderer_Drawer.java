package net.raphimc.immediatelyfast.injection.mixins.fast_text_lookup;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Font.StringRenderOutput.class })
public abstract class MixinTextRenderer_Drawer {

    @Unique
    private RenderType immediatelyFast$lastRenderLayer;

    @Unique
    private VertexConsumer immediatelyFast$lastVertexConsumer;

    @Unique
    private ResourceLocation immediatelyFast$lastFont;

    @Unique
    private FontSet immediatelyFast$lastFontStorage;

    @Redirect(method = { "accept" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer reduceGetBufferCalls(MultiBufferSource instance, RenderType renderLayer) {
        boolean var10000;
        label18: {
            if (this.immediatelyFast$lastVertexConsumer instanceof BufferBuilder bufferBuilder && !bufferBuilder.building()) {
                var10000 = true;
                break label18;
            }
            var10000 = false;
        }
        boolean isBufferInvalid = var10000;
        if (!isBufferInvalid && this.immediatelyFast$lastRenderLayer == renderLayer) {
            return this.immediatelyFast$lastVertexConsumer;
        } else {
            this.immediatelyFast$lastRenderLayer = renderLayer;
            return this.immediatelyFast$lastVertexConsumer = instance.getBuffer(renderLayer);
        }
    }

    @Redirect(method = { "accept" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getFontStorage(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/font/FontStorage;"))
    private FontSet reduceGetFontStorageCalls(Font instance, ResourceLocation id) {
        if (this.immediatelyFast$lastFont == id) {
            return this.immediatelyFast$lastFontStorage;
        } else {
            this.immediatelyFast$lastFont = id;
            return this.immediatelyFast$lastFontStorage = instance.getFontSet(id);
        }
    }
}