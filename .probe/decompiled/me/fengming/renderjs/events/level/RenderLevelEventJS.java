package me.fengming.renderjs.events.level;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;

@Info("Invoked on rendering the world.\n")
public class RenderLevelEventJS extends RenderEventJS {

    private final LevelRenderer levelRenderer;

    private final PoseStack poseStack;

    private final Matrix4f projectionMatrix;

    private final int renderTick;

    private final float partialTick;

    private final Camera camera;

    private final Frustum frustum;

    protected RenderLevelEventJS(LevelRenderer renderer, PoseStack poseStack, Matrix4f matrix4f, int renderTick, float partialTick, Camera camera, Frustum frustum) {
        this.levelRenderer = renderer;
        this.poseStack = poseStack;
        this.projectionMatrix = matrix4f;
        this.renderTick = renderTick;
        this.partialTick = partialTick;
        this.camera = camera;
        this.frustum = frustum;
    }

    public LevelRenderer getLevelRenderer() {
        return this.levelRenderer;
    }

    @Override
    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public int getRenderTick() {
        return this.renderTick;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Frustum getFrustum() {
        return this.frustum;
    }

    public void renderInWorldXyz(String id, float x, float y, float z) {
        super.renderInWorldCameraXYZ(id, this.camera, x, y, z);
    }

    @Info("Invoked after rendering the world.\n")
    public static class After extends RenderLevelEventJS {

        public After(LevelRenderer renderer, PoseStack poseStack, Matrix4f matrix4f, int renderTick, float partialTick, Camera camera, Frustum frustum) {
            super(renderer, poseStack, matrix4f, renderTick, partialTick, camera, frustum);
        }
    }

    @Info("Invoked before rendering the world.\n")
    public static class Before extends RenderLevelEventJS {

        public Before(LevelRenderer renderer, PoseStack poseStack, Matrix4f matrix4f, int renderTick, float partialTick, Camera camera, Frustum frustum) {
            super(renderer, poseStack, matrix4f, renderTick, partialTick, camera, frustum);
        }
    }
}