package me.jellysquid.mods.sodium.mixin.features.render.immediate.buffer_builder;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import me.jellysquid.mods.sodium.client.buffer.ExtendedVertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BufferBuilder.class })
public abstract class BufferBuilderMixin extends DefaultedVertexConsumer implements BufferVertexConsumer {

    @Shadow
    private VertexFormatElement currentElement;

    @Shadow
    private int nextElementByte;

    @Shadow
    private int elementIndex;

    private ExtendedVertexFormat.Element[] embeddium$vertexFormatExtendedElements;

    private ExtendedVertexFormat.Element embeddium$currentExtendedElement;

    @Inject(method = { "switchFormat" }, at = { @At(value = "FIELD", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;format:Lcom/mojang/blaze3d/vertex/VertexFormat;", opcode = 181) })
    private void onFormatChanged(VertexFormat format, CallbackInfo ci) {
        this.embeddium$vertexFormatExtendedElements = ((ExtendedVertexFormat) format).embeddium$getExtendedElements();
        this.embeddium$currentExtendedElement = this.embeddium$vertexFormatExtendedElements[0];
    }

    @Overwrite
    @Override
    public void nextElement() {
        if ((this.elementIndex = this.elementIndex + this.embeddium$currentExtendedElement.increment) >= this.embeddium$vertexFormatExtendedElements.length) {
            this.elementIndex = this.elementIndex - this.embeddium$vertexFormatExtendedElements.length;
        }
        this.nextElementByte = this.nextElementByte + this.embeddium$currentExtendedElement.byteLength;
        this.embeddium$currentExtendedElement = this.embeddium$vertexFormatExtendedElements[this.elementIndex];
        this.currentElement = this.embeddium$currentExtendedElement.actual;
        if (this.f_85824_ && this.currentElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            BufferVertexConsumer.super.color(this.f_85825_, this.f_85826_, this.f_85827_, this.f_85828_);
        }
    }
}