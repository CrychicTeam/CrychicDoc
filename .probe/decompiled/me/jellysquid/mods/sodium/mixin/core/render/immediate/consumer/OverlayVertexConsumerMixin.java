package me.jellysquid.mods.sodium.mixin.core.render.immediate.consumer;

import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.NormI8;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.TextureAttribute;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SheetedDecalTextureGenerator.class })
public class OverlayVertexConsumerMixin implements VertexBufferWriter {

    @Shadow
    @Final
    private VertexConsumer delegate;

    @Shadow
    @Final
    private Matrix3f normalInversePose;

    @Shadow
    @Final
    private Matrix4f cameraInversePose;

    @Shadow
    @Final
    private float textureScale;

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
        transform(ptr, count, format, this.normalInversePose, this.cameraInversePose, this.textureScale);
        VertexBufferWriter.of(this.delegate).push(stack, ptr, count, format);
    }

    @Unique
    private static void transform(long ptr, int count, VertexFormatDescription format, Matrix3f inverseNormalMatrix, Matrix4f inverseTextureMatrix, float textureScale) {
        long stride = (long) format.stride();
        int offsetPosition = format.getElementOffset(CommonVertexAttribute.POSITION);
        int offsetColor = format.getElementOffset(CommonVertexAttribute.COLOR);
        int offsetNormal = format.getElementOffset(CommonVertexAttribute.NORMAL);
        int offsetTexture = format.getElementOffset(CommonVertexAttribute.TEXTURE);
        int color = ColorABGR.pack(1.0F, 1.0F, 1.0F, 1.0F);
        Vector3f normal = new Vector3f(Float.NaN);
        Vector4f position = new Vector4f(Float.NaN);
        for (int vertexIndex = 0; vertexIndex < count; vertexIndex++) {
            position.x = MemoryUtil.memGetFloat(ptr + (long) offsetPosition + 0L);
            position.y = MemoryUtil.memGetFloat(ptr + (long) offsetPosition + 4L);
            position.z = MemoryUtil.memGetFloat(ptr + (long) offsetPosition + 8L);
            position.w = 1.0F;
            int packedNormal = MemoryUtil.memGetInt(ptr + (long) offsetNormal);
            normal.x = NormI8.unpackX(packedNormal);
            normal.y = NormI8.unpackY(packedNormal);
            normal.z = NormI8.unpackZ(packedNormal);
            Vector3f transformedNormal = inverseNormalMatrix.transform(normal);
            Direction direction = Direction.getNearest(transformedNormal.x(), transformedNormal.y(), transformedNormal.z());
            Vector4f transformedTexture = inverseTextureMatrix.transform(position);
            transformedTexture.rotateY((float) Math.PI);
            transformedTexture.rotateX((float) (-Math.PI / 2));
            transformedTexture.rotate(direction.getRotation());
            float textureU = -transformedTexture.x() * textureScale;
            float textureV = -transformedTexture.y() * textureScale;
            ColorAttribute.set(ptr + (long) offsetColor, color);
            TextureAttribute.put(ptr + (long) offsetTexture, textureU, textureV);
            ptr += stride;
        }
    }
}