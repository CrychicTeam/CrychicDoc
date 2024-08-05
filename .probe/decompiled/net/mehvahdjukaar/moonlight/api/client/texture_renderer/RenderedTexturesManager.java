package net.mehvahdjukaar.moonlight.api.client.texture_renderer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class RenderedTexturesManager {

    private static final LoadingCache<ResourceLocation, FrameBufferBackedDynamicTexture> TEXTURE_CACHE = CacheBuilder.newBuilder().removalListener(i -> RenderSystem.recordRenderCall(((FrameBufferBackedDynamicTexture) i.getValue())::close)).expireAfterAccess(2L, TimeUnit.MINUTES).build(new CacheLoader<ResourceLocation, FrameBufferBackedDynamicTexture>() {

        public FrameBufferBackedDynamicTexture load(ResourceLocation key) {
            return null;
        }
    });

    public static void clearCache() {
        TEXTURE_CACHE.invalidateAll();
    }

    public static FrameBufferBackedDynamicTexture requestTexture(ResourceLocation id, int textureSize, Consumer<FrameBufferBackedDynamicTexture> textureDrawingFunction, boolean updateEachFrame) {
        FrameBufferBackedDynamicTexture texture = (FrameBufferBackedDynamicTexture) TEXTURE_CACHE.getIfPresent(id);
        if (texture == null) {
            texture = (FrameBufferBackedDynamicTexture) (updateEachFrame ? new TickableFrameBufferBackedDynamicTexture(id, textureSize, textureDrawingFunction) : new FrameBufferBackedDynamicTexture(id, textureSize, textureDrawingFunction));
            TEXTURE_CACHE.put(id, texture);
            RenderSystem.recordRenderCall(texture::initialize);
        }
        return texture;
    }

    public static FrameBufferBackedDynamicTexture requestFlatItemStackTexture(ResourceLocation res, ItemStack stack, int size) {
        return requestTexture(res, size, t -> drawItem(t, stack), true);
    }

    public static FrameBufferBackedDynamicTexture requestFlatItemTexture(Item item, int size) {
        return requestFlatItemTexture(item, size, null);
    }

    public static FrameBufferBackedDynamicTexture requestFlatItemTexture(Item item, int size, @Nullable Consumer<NativeImage> postProcessing) {
        ResourceLocation id = Moonlight.res(Utils.getID(item).toString().replace(":", "/") + "/" + size);
        return requestFlatItemTexture(id, item, size, postProcessing, false);
    }

    public static FrameBufferBackedDynamicTexture requestFlatItemTexture(ResourceLocation id, Item item, int size, @Nullable Consumer<NativeImage> postProcessing) {
        return requestFlatItemTexture(id, item, size, postProcessing, false);
    }

    public static FrameBufferBackedDynamicTexture requestFlatItemTexture(ResourceLocation id, Item item, int size, @Nullable Consumer<NativeImage> postProcessing, boolean updateEachFrame) {
        return requestTexture(id, size, t -> {
            drawItem(t, item.getDefaultInstance());
            if (postProcessing != null) {
                t.download();
                NativeImage img = t.getPixels();
                postProcessing.accept(img);
                t.upload();
            }
        }, updateEachFrame);
    }

    public static void drawItem(FrameBufferBackedDynamicTexture tex, ItemStack stack) {
        drawAsInGUI(tex, s -> RenderUtil.getGuiDummy(s).renderFakeItem(stack, 0, 0));
    }

    public static void drawTexture(FrameBufferBackedDynamicTexture tex, ResourceLocation texture) {
        drawAsInGUI(tex, s -> {
            RenderSystem.setShaderTexture(0, texture);
            Matrix4f matrix = s.last().pose();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableBlend();
            RenderSystem.setShader(GameRenderer::m_172838_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.m_252986_(matrix, 0.0F, 16.0F, 0.0F).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.m_252986_(matrix, 16.0F, 16.0F, 0.0F).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.m_252986_(matrix, 16.0F, 0.0F, 0.0F).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.m_252986_(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferBuilder.end());
        });
    }

    public static void drawNormalized(FrameBufferBackedDynamicTexture tex, Consumer<PoseStack> drawFunction) {
        drawAsInGUI(tex, s -> {
            float scale = 0.0625F;
            s.translate(8.0F, 8.0F, 0.0F);
            s.scale(scale, scale, 1.0F);
            drawFunction.accept(s);
        });
    }

    public static void drawAsInGUI(FrameBufferBackedDynamicTexture tex, Consumer<PoseStack> drawFunction) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget frameBuffer = tex.getFrameBuffer();
        frameBuffer.clear(Minecraft.ON_OSX);
        frameBuffer.bindWrite(true);
        int size = 16;
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, (float) size, (float) size, 0.0F, -1000.0F, 1000.0F);
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.setIdentity();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        drawFunction.accept(new PoseStack());
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.restoreProjectionMatrix();
        mc.getMainRenderTarget().bindWrite(true);
    }
}