package me.jellysquid.mods.sodium.mixin.features.render.immediate.buffer_builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import me.jellysquid.mods.sodium.client.buffer.ExtendedVertexFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ VertexFormat.class })
public class VertexFormatMixin implements ExtendedVertexFormat {

    @Shadow
    @Final
    private ImmutableList<VertexFormatElement> elements;

    private ExtendedVertexFormat.Element[] embeddium$extendedElements;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void embeddium$createElementArray(ImmutableMap<String, VertexFormatElement> immutableList, CallbackInfo ci) {
        this.embeddium$extendedElements = new ExtendedVertexFormat.Element[this.elements.size()];
        if (this.elements.size() != 0) {
            VertexFormatElement currentElement = (VertexFormatElement) this.elements.get(0);
            int id = 0;
            UnmodifiableIterator var5 = this.elements.iterator();
            while (var5.hasNext()) {
                VertexFormatElement element = (VertexFormatElement) var5.next();
                if (element.getUsage() != VertexFormatElement.Usage.PADDING) {
                    int oldId = id;
                    int byteLength = 0;
                    do {
                        if (++id >= this.embeddium$extendedElements.length) {
                            id -= this.embeddium$extendedElements.length;
                        }
                        byteLength += currentElement.getByteSize();
                        currentElement = (VertexFormatElement) this.elements.get(id);
                    } while (currentElement.getUsage() == VertexFormatElement.Usage.PADDING);
                    this.embeddium$extendedElements[oldId] = new ExtendedVertexFormat.Element(element, id - oldId, byteLength);
                }
            }
        }
    }

    @Override
    public ExtendedVertexFormat.Element[] embeddium$getExtendedElements() {
        return this.embeddium$extendedElements;
    }
}