package me.jellysquid.mods.sodium.client.render.vertex.buffer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.util.NormI8;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.LightAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.NormalAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.OverlayAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.PositionAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.TextureAttribute;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Unique;

public class SodiumBufferBuilder implements VertexConsumer, VertexBufferWriter {

    private static final int ATTRIBUTE_NOT_PRESENT = -1;

    private static final int ATTRIBUTE_POSITION_BIT = 1 << CommonVertexAttribute.POSITION.ordinal();

    private static final int ATTRIBUTE_COLOR_BIT = 1 << CommonVertexAttribute.COLOR.ordinal();

    private static final int ATTRIBUTE_TEXTURE_BIT = 1 << CommonVertexAttribute.TEXTURE.ordinal();

    private static final int ATTRIBUTE_OVERLAY_BIT = 1 << CommonVertexAttribute.OVERLAY.ordinal();

    private static final int ATTRIBUTE_LIGHT_BIT = 1 << CommonVertexAttribute.LIGHT.ordinal();

    private static final int ATTRIBUTE_NORMAL_BIT = 1 << CommonVertexAttribute.NORMAL.ordinal();

    private final ExtendedBufferBuilder builder;

    private int attributeOffsetPosition;

    private int attributeOffsetColor;

    private int attributeOffsetTexture;

    private int attributeOffsetOverlay;

    private int attributeOffsetLight;

    private int attributeOffsetNormal;

    private int requiredAttributes;

    private int writtenAttributes;

    private int packedFixedColor;

    public SodiumBufferBuilder(ExtendedBufferBuilder builder) {
        this.builder = builder;
        this.resetAttributeBindings();
        this.updateAttributeBindings(this.builder.sodium$getFormatDescription());
    }

    private void resetAttributeBindings() {
        this.requiredAttributes = 0;
        this.attributeOffsetPosition = -1;
        this.attributeOffsetColor = -1;
        this.attributeOffsetTexture = -1;
        this.attributeOffsetOverlay = -1;
        this.attributeOffsetLight = -1;
        this.attributeOffsetNormal = -1;
    }

    private void updateAttributeBindings(VertexFormatDescription desc) {
        this.resetAttributeBindings();
        if (desc.containsElement(CommonVertexAttribute.POSITION)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_POSITION_BIT;
            this.attributeOffsetPosition = desc.getElementOffset(CommonVertexAttribute.POSITION);
        }
        if (desc.containsElement(CommonVertexAttribute.COLOR)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_COLOR_BIT;
            this.attributeOffsetColor = desc.getElementOffset(CommonVertexAttribute.COLOR);
        }
        if (desc.containsElement(CommonVertexAttribute.TEXTURE)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_TEXTURE_BIT;
            this.attributeOffsetTexture = desc.getElementOffset(CommonVertexAttribute.TEXTURE);
        }
        if (desc.containsElement(CommonVertexAttribute.OVERLAY)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_OVERLAY_BIT;
            this.attributeOffsetOverlay = desc.getElementOffset(CommonVertexAttribute.OVERLAY);
        }
        if (desc.containsElement(CommonVertexAttribute.LIGHT)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_LIGHT_BIT;
            this.attributeOffsetLight = desc.getElementOffset(CommonVertexAttribute.LIGHT);
        }
        if (desc.containsElement(CommonVertexAttribute.NORMAL)) {
            this.requiredAttributes = this.requiredAttributes | ATTRIBUTE_NORMAL_BIT;
            this.attributeOffsetNormal = desc.getElementOffset(CommonVertexAttribute.NORMAL);
        }
    }

    @Override
    public void push(MemoryStack stack, long ptr, int count, VertexFormatDescription format) {
        this.builder.push(stack, ptr, count, format);
    }

    @Override
    public boolean canUseIntrinsics() {
        return this.builder.canUseIntrinsics();
    }

    @Unique
    private void putPositionAttribute(float x, float y, float z) {
        if (this.attributeOffsetPosition != -1 && (this.writtenAttributes & ATTRIBUTE_POSITION_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetPosition);
            PositionAttribute.put(offset, x, y, z);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_POSITION_BIT;
        }
    }

    @Unique
    private void putColorAttribute(int rgba) {
        if (this.attributeOffsetColor != -1 && (this.writtenAttributes & ATTRIBUTE_COLOR_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetColor);
            ColorAttribute.set(offset, rgba);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_COLOR_BIT;
        }
    }

    @Unique
    private void putTextureAttribute(float u, float v) {
        if (this.attributeOffsetTexture != -1 && (this.writtenAttributes & ATTRIBUTE_TEXTURE_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetTexture);
            TextureAttribute.put(offset, u, v);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_TEXTURE_BIT;
        }
    }

    @Unique
    private void putOverlayAttribute(int uv) {
        if (this.attributeOffsetOverlay != -1 && (this.writtenAttributes & ATTRIBUTE_OVERLAY_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetOverlay);
            OverlayAttribute.set(offset, uv);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_OVERLAY_BIT;
        }
    }

    @Unique
    private void putLightAttribute(int uv) {
        if (this.attributeOffsetLight != -1 && (this.writtenAttributes & ATTRIBUTE_LIGHT_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetLight);
            LightAttribute.set(offset, uv);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_LIGHT_BIT;
        }
    }

    @Unique
    private void putNormalAttribute(int normal) {
        if (this.attributeOffsetNormal != -1 && (this.writtenAttributes & ATTRIBUTE_NORMAL_BIT) == 0) {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset() + this.attributeOffsetNormal);
            NormalAttribute.set(offset, normal);
            this.writtenAttributes = this.writtenAttributes | ATTRIBUTE_NORMAL_BIT;
        }
    }

    @Override
    public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        if (this.builder.sodium$usingFixedColor()) {
            throw new IllegalStateException();
        } else {
            long offset = MemoryUtil.memAddress(this.builder.sodium$getBuffer(), this.builder.sodium$getElementOffset());
            if (this.attributeOffsetPosition != -1) {
                PositionAttribute.put(offset + (long) this.attributeOffsetPosition, x, y, z);
            }
            if (this.attributeOffsetColor != -1) {
                ColorAttribute.set(offset + (long) this.attributeOffsetColor, ColorABGR.pack(red, green, blue, alpha));
            }
            if (this.attributeOffsetTexture != -1) {
                TextureAttribute.put(offset + (long) this.attributeOffsetTexture, u, v);
            }
            if (this.attributeOffsetOverlay != -1) {
                OverlayAttribute.set(offset + (long) this.attributeOffsetOverlay, overlay);
            }
            if (this.attributeOffsetLight != -1) {
                LightAttribute.set(offset + (long) this.attributeOffsetLight, light);
            }
            if (this.attributeOffsetNormal != -1) {
                NormalAttribute.set(offset + (long) this.attributeOffsetNormal, NormI8.pack(normalX, normalY, normalZ));
            }
            this.writtenAttributes = ATTRIBUTE_POSITION_BIT | ATTRIBUTE_COLOR_BIT | ATTRIBUTE_TEXTURE_BIT | ATTRIBUTE_OVERLAY_BIT | ATTRIBUTE_LIGHT_BIT | ATTRIBUTE_NORMAL_BIT;
            this.endVertex();
        }
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        ((BufferBuilder) this.builder).m_7404_(red, green, blue, alpha);
        this.packedFixedColor = ColorABGR.pack(red, green, blue, alpha);
    }

    @Override
    public void unsetDefaultColor() {
        ((BufferBuilder) this.builder).m_141991_();
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.putPositionAttribute((float) x, (float) y, (float) z);
        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        if (this.builder.sodium$usingFixedColor()) {
            throw new IllegalStateException();
        } else {
            this.putColorAttribute(ColorABGR.pack(red, green, blue, alpha));
            return this;
        }
    }

    @Override
    public VertexConsumer color(int argb) {
        if (this.builder.sodium$usingFixedColor()) {
            throw new IllegalStateException();
        } else {
            this.putColorAttribute(ColorARGB.toABGR(argb));
            return this;
        }
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        this.putTextureAttribute(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int uv) {
        this.putOverlayAttribute(uv);
        return this;
    }

    @Override
    public VertexConsumer uv2(int uv) {
        this.putLightAttribute(uv);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        this.putNormalAttribute(NormI8.pack(x, y, z));
        return this;
    }

    @Override
    public void endVertex() {
        if (this.builder.sodium$usingFixedColor()) {
            this.putColorAttribute(this.packedFixedColor);
        }
        if (!this.isVertexFinished()) {
            throw new IllegalStateException("Not filled all elements of the vertex");
        } else {
            this.builder.sodium$moveToNextVertex();
            this.writtenAttributes = 0;
        }
    }

    public void reset() {
        this.writtenAttributes = 0;
    }

    public BufferBuilder getOriginalBufferBuilder() {
        return (BufferBuilder) this.builder;
    }

    private boolean isVertexFinished() {
        return (this.writtenAttributes & this.requiredAttributes) == this.requiredAttributes;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        return this.uv2(packU16x2(u, v));
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return this.overlayCoords(packU16x2(u, v));
    }

    @Unique
    private static int packU16x2(int u, int v) {
        return (u & 65535) << 0 | (v & 65535) << 16;
    }
}