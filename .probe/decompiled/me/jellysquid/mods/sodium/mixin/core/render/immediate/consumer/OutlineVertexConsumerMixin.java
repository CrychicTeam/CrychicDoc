package me.jellysquid.mods.sodium.mixin.core.render.immediate.consumer;

import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "net/minecraft/client/renderer/OutlineBufferSource$EntityOutlineGenerator" })
public abstract class OutlineVertexConsumerMixin extends DefaultedVertexConsumer implements VertexBufferWriter {

    @Shadow
    @Final
    private VertexConsumer delegate;

    @Unique
    private boolean isFullWriter;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void onInit(CallbackInfo ci) {
        this.isFullWriter = VertexBufferWriter.tryOf(this.delegate) != null;
    }

    @Override
    public boolean canUseIntrinsics() {
        return this.isFullWriter;
    }

    @Override
    public void push(MemoryStack stack, long ptr, int count, VertexFormatDescription format) {
        transform(ptr, count, format, ColorABGR.pack(this.f_85825_, this.f_85826_, this.f_85827_, this.f_85828_));
        VertexBufferWriter.of(this.delegate).push(stack, ptr, count, format);
    }

    @Unique
    private static void transform(long ptr, int count, VertexFormatDescription format, int color) {
        long stride = (long) format.stride();
        long offsetColor = (long) format.getElementOffset(CommonVertexAttribute.COLOR);
        for (int vertexIndex = 0; vertexIndex < count; vertexIndex++) {
            ColorAttribute.set(ptr + offsetColor, color);
            ptr += stride;
        }
    }
}