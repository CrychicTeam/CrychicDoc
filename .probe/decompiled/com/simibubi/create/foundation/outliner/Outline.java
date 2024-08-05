package com.simibubi.create.foundation.outliner;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Color;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Outline {

    protected final Outline.OutlineParams params;

    protected final Vector4f colorTemp = new Vector4f();

    protected final Vector3f diffPosTemp = new Vector3f();

    protected final Vector3f minPosTemp = new Vector3f();

    protected final Vector3f maxPosTemp = new Vector3f();

    protected final Vector4f posTransformTemp = new Vector4f();

    protected final Vector3f normalTransformTemp = new Vector3f();

    public Outline() {
        this.params = new Outline.OutlineParams();
    }

    public Outline.OutlineParams getParams() {
        return this.params;
    }

    public abstract void render(PoseStack var1, SuperRenderTypeBuffer var2, Vec3 var3, float var4);

    public void tick() {
    }

    public void bufferCuboidLine(PoseStack poseStack, VertexConsumer consumer, Vec3 camera, Vector3d start, Vector3d end, float width, Vector4f color, int lightmap, boolean disableNormals) {
        Vector3f diff = this.diffPosTemp;
        diff.set((float) (end.x - start.x), (float) (end.y - start.y), (float) (end.z - start.z));
        float length = Mth.sqrt(diff.x() * diff.x() + diff.y() * diff.y() + diff.z() * diff.z());
        float hAngle = AngleHelper.deg(Mth.atan2((double) diff.x(), (double) diff.z()));
        float hDistance = Mth.sqrt(diff.x() * diff.x() + diff.z() * diff.z());
        float vAngle = AngleHelper.deg(Mth.atan2((double) hDistance, (double) diff.y())) - 90.0F;
        poseStack.pushPose();
        ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).translate(start.x - camera.x, start.y - camera.y, start.z - camera.z)).rotateY((double) hAngle)).rotateX((double) vAngle);
        this.bufferCuboidLine(poseStack.last(), consumer, new Vector3f(), Direction.SOUTH, length, width, color, lightmap, disableNormals);
        poseStack.popPose();
    }

    public void bufferCuboidLine(PoseStack.Pose pose, VertexConsumer consumer, Vector3f origin, Direction direction, float length, float width, Vector4f color, int lightmap, boolean disableNormals) {
        Vector3f minPos = this.minPosTemp;
        Vector3f maxPos = this.maxPosTemp;
        float halfWidth = width / 2.0F;
        minPos.set(origin.x() - halfWidth, origin.y() - halfWidth, origin.z() - halfWidth);
        maxPos.set(origin.x() + halfWidth, origin.y() + halfWidth, origin.z() + halfWidth);
        switch(direction) {
            case DOWN:
                minPos.add(0.0F, -length, 0.0F);
                break;
            case UP:
                maxPos.add(0.0F, length, 0.0F);
                break;
            case NORTH:
                minPos.add(0.0F, 0.0F, -length);
                break;
            case SOUTH:
                maxPos.add(0.0F, 0.0F, length);
                break;
            case WEST:
                minPos.add(-length, 0.0F, 0.0F);
                break;
            case EAST:
                maxPos.add(length, 0.0F, 0.0F);
        }
        this.bufferCuboid(pose, consumer, minPos, maxPos, color, lightmap, disableNormals);
    }

    public void bufferCuboid(PoseStack.Pose pose, VertexConsumer consumer, Vector3f minPos, Vector3f maxPos, Vector4f color, int lightmap, boolean disableNormals) {
        Vector4f posTransformTemp = this.posTransformTemp;
        Vector3f normalTransformTemp = this.normalTransformTemp;
        float minX = minPos.x();
        float minY = minPos.y();
        float minZ = minPos.z();
        float maxX = maxPos.x();
        float maxY = maxPos.y();
        float maxZ = maxPos.z();
        Matrix4f posMatrix = pose.pose();
        posTransformTemp.set(minX, minY, maxZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x0 = (double) posTransformTemp.x();
        double y0 = (double) posTransformTemp.y();
        double z0 = (double) posTransformTemp.z();
        posTransformTemp.set(minX, minY, minZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x1 = (double) posTransformTemp.x();
        double y1 = (double) posTransformTemp.y();
        double z1 = (double) posTransformTemp.z();
        posTransformTemp.set(maxX, minY, minZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x2 = (double) posTransformTemp.x();
        double y2 = (double) posTransformTemp.y();
        double z2 = (double) posTransformTemp.z();
        posTransformTemp.set(maxX, minY, maxZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x3 = (double) posTransformTemp.x();
        double y3 = (double) posTransformTemp.y();
        double z3 = (double) posTransformTemp.z();
        posTransformTemp.set(minX, maxY, minZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x4 = (double) posTransformTemp.x();
        double y4 = (double) posTransformTemp.y();
        double z4 = (double) posTransformTemp.z();
        posTransformTemp.set(minX, maxY, maxZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x5 = (double) posTransformTemp.x();
        double y5 = (double) posTransformTemp.y();
        double z5 = (double) posTransformTemp.z();
        posTransformTemp.set(maxX, maxY, maxZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x6 = (double) posTransformTemp.x();
        double y6 = (double) posTransformTemp.y();
        double z6 = (double) posTransformTemp.z();
        posTransformTemp.set(maxX, maxY, minZ, 1.0F);
        posTransformTemp.mul(posMatrix);
        double x7 = (double) posTransformTemp.x();
        double y7 = (double) posTransformTemp.y();
        double z7 = (double) posTransformTemp.z();
        float r = color.x();
        float g = color.y();
        float b = color.z();
        float a = color.w();
        Matrix3f normalMatrix = pose.normal();
        if (disableNormals) {
            normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        } else {
            normalTransformTemp.set(0.0F, -1.0F, 0.0F);
        }
        normalTransformTemp.mul(normalMatrix);
        float nx0 = normalTransformTemp.x();
        float ny0 = normalTransformTemp.y();
        float nz0 = normalTransformTemp.z();
        consumer.vertex(x0, y0, z0).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx0, ny0, nz0).endVertex();
        consumer.vertex(x1, y1, z1).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx0, ny0, nz0).endVertex();
        consumer.vertex(x2, y2, z2).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx0, ny0, nz0).endVertex();
        consumer.vertex(x3, y3, z3).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx0, ny0, nz0).endVertex();
        normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        normalTransformTemp.mul(normalMatrix);
        float nx1 = normalTransformTemp.x();
        float ny1 = normalTransformTemp.y();
        float nz1 = normalTransformTemp.z();
        consumer.vertex(x4, y4, z4).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx1, ny1, nz1).endVertex();
        consumer.vertex(x5, y5, z5).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx1, ny1, nz1).endVertex();
        consumer.vertex(x6, y6, z6).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx1, ny1, nz1).endVertex();
        consumer.vertex(x7, y7, z7).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx1, ny1, nz1).endVertex();
        if (disableNormals) {
            normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        } else {
            normalTransformTemp.set(0.0F, 0.0F, -1.0F);
        }
        normalTransformTemp.mul(normalMatrix);
        float nx2 = normalTransformTemp.x();
        float ny2 = normalTransformTemp.y();
        float nz2 = normalTransformTemp.z();
        consumer.vertex(x7, y7, z7).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx2, ny2, nz2).endVertex();
        consumer.vertex(x2, y2, z2).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx2, ny2, nz2).endVertex();
        consumer.vertex(x1, y1, z1).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx2, ny2, nz2).endVertex();
        consumer.vertex(x4, y4, z4).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx2, ny2, nz2).endVertex();
        if (disableNormals) {
            normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        } else {
            normalTransformTemp.set(0.0F, 0.0F, 1.0F);
        }
        normalTransformTemp.mul(normalMatrix);
        float nx3 = normalTransformTemp.x();
        float ny3 = normalTransformTemp.y();
        float nz3 = normalTransformTemp.z();
        consumer.vertex(x5, y5, z5).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx3, ny3, nz3).endVertex();
        consumer.vertex(x0, y0, z0).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx3, ny3, nz3).endVertex();
        consumer.vertex(x3, y3, z3).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx3, ny3, nz3).endVertex();
        consumer.vertex(x6, y6, z6).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx3, ny3, nz3).endVertex();
        if (disableNormals) {
            normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        } else {
            normalTransformTemp.set(-1.0F, 0.0F, 0.0F);
        }
        normalTransformTemp.mul(normalMatrix);
        float nx4 = normalTransformTemp.x();
        float ny4 = normalTransformTemp.y();
        float nz4 = normalTransformTemp.z();
        consumer.vertex(x4, y4, z4).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx4, ny4, nz4).endVertex();
        consumer.vertex(x1, y1, z1).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx4, ny4, nz4).endVertex();
        consumer.vertex(x0, y0, z0).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx4, ny4, nz4).endVertex();
        consumer.vertex(x5, y5, z5).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx4, ny4, nz4).endVertex();
        if (disableNormals) {
            normalTransformTemp.set(0.0F, 1.0F, 0.0F);
        } else {
            normalTransformTemp.set(1.0F, 0.0F, 0.0F);
        }
        normalTransformTemp.mul(normalMatrix);
        float nx5 = normalTransformTemp.x();
        float ny5 = normalTransformTemp.y();
        float nz5 = normalTransformTemp.z();
        consumer.vertex(x6, y6, z6).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx5, ny5, nz5).endVertex();
        consumer.vertex(x3, y3, z3).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx5, ny5, nz5).endVertex();
        consumer.vertex(x2, y2, z2).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx5, ny5, nz5).endVertex();
        consumer.vertex(x7, y7, z7).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx5, ny5, nz5).endVertex();
    }

    public void bufferQuad(PoseStack.Pose pose, VertexConsumer consumer, Vector3f pos0, Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector4f color, int lightmap, Vector3f normal) {
        this.bufferQuad(pose, consumer, pos0, pos1, pos2, pos3, color, 0.0F, 0.0F, 1.0F, 1.0F, lightmap, normal);
    }

    public void bufferQuad(PoseStack.Pose pose, VertexConsumer consumer, Vector3f pos0, Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector4f color, float minU, float minV, float maxU, float maxV, int lightmap, Vector3f normal) {
        Vector4f posTransformTemp = this.posTransformTemp;
        Vector3f normalTransformTemp = this.normalTransformTemp;
        Matrix4f posMatrix = pose.pose();
        posTransformTemp.set(pos0.x(), pos0.y(), pos0.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x0 = (double) posTransformTemp.x();
        double y0 = (double) posTransformTemp.y();
        double z0 = (double) posTransformTemp.z();
        posTransformTemp.set(pos1.x(), pos1.y(), pos1.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x1 = (double) posTransformTemp.x();
        double y1 = (double) posTransformTemp.y();
        double z1 = (double) posTransformTemp.z();
        posTransformTemp.set(pos2.x(), pos2.y(), pos2.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x2 = (double) posTransformTemp.x();
        double y2 = (double) posTransformTemp.y();
        double z2 = (double) posTransformTemp.z();
        posTransformTemp.set(pos3.x(), pos3.y(), pos3.z(), 1.0F);
        posTransformTemp.mul(posMatrix);
        double x3 = (double) posTransformTemp.x();
        double y3 = (double) posTransformTemp.y();
        double z3 = (double) posTransformTemp.z();
        float r = color.x();
        float g = color.y();
        float b = color.z();
        float a = color.w();
        normalTransformTemp.set(normal);
        normalTransformTemp.mul(pose.normal());
        float nx = normalTransformTemp.x();
        float ny = normalTransformTemp.y();
        float nz = normalTransformTemp.z();
        consumer.vertex(x0, y0, z0).color(r, g, b, a).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx, ny, nz).endVertex();
        consumer.vertex(x1, y1, z1).color(r, g, b, a).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx, ny, nz).endVertex();
        consumer.vertex(x2, y2, z2).color(r, g, b, a).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx, ny, nz).endVertex();
        consumer.vertex(x3, y3, z3).color(r, g, b, a).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightmap).normal(nx, ny, nz).endVertex();
    }

    public static class OutlineParams {

        protected Optional<AllSpecialTextures> faceTexture;

        protected Optional<AllSpecialTextures> hightlightedFaceTexture;

        protected Direction highlightedFace;

        protected boolean fadeLineWidth;

        protected boolean disableCull;

        protected boolean disableLineNormals;

        protected float alpha;

        protected int lightmap;

        protected Color rgb;

        private float lineWidth;

        public OutlineParams() {
            this.faceTexture = this.hightlightedFaceTexture = Optional.empty();
            this.alpha = 1.0F;
            this.lineWidth = 0.03125F;
            this.fadeLineWidth = true;
            this.rgb = Color.WHITE;
            this.lightmap = 15728880;
        }

        public Outline.OutlineParams colored(int color) {
            this.rgb = new Color(color, false);
            return this;
        }

        public Outline.OutlineParams colored(Color c) {
            this.rgb = c.copy();
            return this;
        }

        public Outline.OutlineParams lightmap(int light) {
            this.lightmap = light;
            return this;
        }

        public Outline.OutlineParams lineWidth(float width) {
            this.lineWidth = width;
            return this;
        }

        public Outline.OutlineParams withFaceTexture(AllSpecialTextures texture) {
            this.faceTexture = Optional.ofNullable(texture);
            return this;
        }

        public Outline.OutlineParams clearTextures() {
            return this.withFaceTextures(null, null);
        }

        public Outline.OutlineParams withFaceTextures(AllSpecialTextures texture, AllSpecialTextures highlightTexture) {
            this.faceTexture = Optional.ofNullable(texture);
            this.hightlightedFaceTexture = Optional.ofNullable(highlightTexture);
            return this;
        }

        public Outline.OutlineParams highlightFace(@Nullable Direction face) {
            this.highlightedFace = face;
            return this;
        }

        public Outline.OutlineParams disableLineNormals() {
            this.disableLineNormals = true;
            return this;
        }

        public Outline.OutlineParams disableCull() {
            this.disableCull = true;
            return this;
        }

        public float getLineWidth() {
            return this.fadeLineWidth ? this.alpha * this.lineWidth : this.lineWidth;
        }

        public Direction getHighlightedFace() {
            return this.highlightedFace;
        }

        public void loadColor(Vector4f vec) {
            vec.set(this.rgb.getRedAsFloat(), this.rgb.getGreenAsFloat(), this.rgb.getBlueAsFloat(), this.rgb.getAlphaAsFloat() * this.alpha);
        }
    }
}