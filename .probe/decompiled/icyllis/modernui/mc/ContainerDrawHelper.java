package icyllis.modernui.mc;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.DrawableInfo;
import icyllis.arc3d.opengl.GLCore;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.CustomDrawable;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.nio.FloatBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public final class ContainerDrawHelper {

    private static final Pools.Pool<ContainerDrawHelper.DrawItem> sDrawItemPool = Pools.newSimplePool(60);

    private final Queue<ContainerDrawHelper.DrawItem> mDrawItems = new ArrayDeque();

    private final FloatBuffer mMatBuf = BufferUtils.createFloatBuffer(16);

    private final Matrix4f mProjection = new Matrix4f();

    private final Object2IntMap<String> mSamplerUnits = new Object2IntArrayMap();

    private final ItemRenderer mRenderer = Minecraft.getInstance().getItemRenderer();

    private final TextureManager mTextureManager = Minecraft.getInstance().getTextureManager();

    private volatile GLSurfaceCanvas mCanvas;

    private ContainerDrawHelper() {
        for (int i = 0; i < 8; i++) {
            this.mSamplerUnits.put("Sampler" + i, i);
        }
        this.mSamplerUnits.defaultReturnValue(-1);
    }

    public static void drawItem(@Nonnull Canvas canvas, @Nonnull ItemStack item, float x, float y, float z, float size, int seed) {
        if (!item.isEmpty()) {
            canvas.drawCustomDrawable(new ContainerDrawHelper.ItemDrawable(item, x, y, z, size, seed));
        }
    }

    private static class DrawItem implements CustomDrawable.DrawHandler {

        private final ItemStack item;

        private final Matrix4f projection;

        private final Matrix4f pose;

        private final int seed;

        private DrawItem(ImageInfo ii, Matrix4 mv, ItemStack is, int seed) {
            this.item = is;
            this.projection = new Matrix4f();
            this.projection.setOrtho(0.0F, (float) ii.width(), (float) ii.height(), 0.0F, 1000.0F, 11000.0F);
            this.pose = new Matrix4f(mv.m11, mv.m12, mv.m13, mv.m14, mv.m21, mv.m22, mv.m23, mv.m24, mv.m31, mv.m32, mv.m33, mv.m34, mv.m41, mv.m42, mv.m43, mv.m44);
            this.seed = seed;
        }

        @Override
        public void draw(DirectContext dContext, DrawableInfo info) {
            Minecraft minecraft = Minecraft.getInstance();
            Matrix4f oldProjection = RenderSystem.getProjectionMatrix();
            RenderSystem.setProjectionMatrix(this.projection, VertexSorting.ORTHOGRAPHIC_Z);
            BufferUploader.invalidate();
            BakedModel model = minecraft.getItemRenderer().getModel(this.item, minecraft.level, minecraft.player, this.seed);
            boolean light2D = !model.usesBlockLight();
            if (light2D) {
                Lighting.setupForFlatItems();
            }
            GLCore.glBindSampler(0, 0);
            RenderSystem.bindTexture(0);
            RenderSystem.enableCull();
            PoseStack localTransform = new PoseStack();
            localTransform.mulPoseMatrix(this.pose);
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            minecraft.getItemRenderer().render(this.item, ItemDisplayContext.GUI, false, localTransform, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
            bufferSource.endBatch();
            if (light2D) {
                Lighting.setupFor3DItems();
            }
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(1, 771, 1, 771);
            RenderSystem.activeTexture(33984);
            RenderSystem.setProjectionMatrix(oldProjection, VertexSorting.ORTHOGRAPHIC_Z);
        }
    }

    public interface FastShader {

        void fastApply(@Nonnull GLSurfaceCanvas var1, @Nonnull Object2IntMap<String> var2);
    }

    private static record ItemDrawable(ItemStack item, float x, float y, float z, float size, int seed) implements CustomDrawable {

        @Override
        public CustomDrawable.DrawHandler snapDrawHandler(int backendApi, Matrix4 viewMatrix, Rect2i clipBounds, ImageInfo targetInfo) {
            viewMatrix.preTranslate(this.x, this.y, this.z + 3000.0F);
            viewMatrix.preScale(this.size, -this.size, this.size);
            return new ContainerDrawHelper.DrawItem(targetInfo, viewMatrix, this.item, this.seed);
        }

        @Override
        public RectF getBounds() {
            return new RectF(-this.size, -this.size, this.size, this.size);
        }
    }
}