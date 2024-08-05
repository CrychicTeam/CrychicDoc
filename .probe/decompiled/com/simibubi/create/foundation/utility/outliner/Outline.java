package com.simibubi.create.foundation.utility.outliner;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public abstract class Outline {

    protected Outline.OutlineParams params = new Outline.OutlineParams();

    protected Matrix3f transformNormals;

    public abstract void render(PoseStack var1, SuperRenderTypeBuffer var2, float var3);

    public void tick() {
    }

    public Outline.OutlineParams getParams() {
        return this.params;
    }

    public void renderCuboidLine(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 start, Vec3 end) {
        Vec3 diff = end.subtract(start);
        float hAngle = AngleHelper.deg(Mth.atan2(diff.x, diff.z));
        float hDistance = (float) diff.multiply(1.0, 0.0, 1.0).length();
        float vAngle = AngleHelper.deg(Mth.atan2((double) hDistance, diff.y)) - 90.0F;
        ms.pushPose();
        ((TransformStack) ((TransformStack) TransformStack.cast(ms).translate(start)).rotateY((double) hAngle)).rotateX((double) vAngle);
        this.renderAACuboidLine(ms, buffer, Vec3.ZERO, new Vec3(0.0, 0.0, diff.length()));
        ms.popPose();
    }

    public void renderAACuboidLine(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 start, Vec3 end) {
        float lineWidth = this.params.getLineWidth();
        if (lineWidth != 0.0F) {
            VertexConsumer builder = buffer.getBuffer(RenderTypes.getOutlineSolid());
            Vec3 diff = end.subtract(start);
            if (diff.x + diff.y + diff.z < 0.0) {
                Vec3 temp = start;
                start = end;
                end = temp;
                diff = diff.scale(-1.0);
            }
            Vec3 extension = diff.normalize().scale((double) (lineWidth / 2.0F));
            Vec3 plane = VecHelper.axisAlingedPlaneOf(diff);
            Direction face = Direction.getNearest(diff.x, diff.y, diff.z);
            Direction.Axis axis = face.getAxis();
            start = start.subtract(extension);
            end = end.add(extension);
            plane = plane.scale((double) (lineWidth / 2.0F));
            Vec3 a1 = plane.add(start);
            Vec3 b1 = plane.add(end);
            plane = VecHelper.rotate(plane, -90.0, axis);
            Vec3 a2 = plane.add(start);
            Vec3 b2 = plane.add(end);
            plane = VecHelper.rotate(plane, -90.0, axis);
            Vec3 a3 = plane.add(start);
            Vec3 b3 = plane.add(end);
            plane = VecHelper.rotate(plane, -90.0, axis);
            Vec3 a4 = plane.add(start);
            Vec3 b4 = plane.add(end);
            if (this.params.disableNormals) {
                face = Direction.UP;
                this.putQuad(ms, builder, b4, b3, b2, b1, face);
                this.putQuad(ms, builder, a1, a2, a3, a4, face);
                this.putQuad(ms, builder, a1, b1, b2, a2, face);
                this.putQuad(ms, builder, a2, b2, b3, a3, face);
                this.putQuad(ms, builder, a3, b3, b4, a4, face);
                this.putQuad(ms, builder, a4, b4, b1, a1, face);
            } else {
                this.putQuad(ms, builder, b4, b3, b2, b1, face);
                this.putQuad(ms, builder, a1, a2, a3, a4, face.getOpposite());
                Vec3 vec = a1.subtract(a4);
                face = Direction.getNearest(vec.x, vec.y, vec.z);
                this.putQuad(ms, builder, a1, b1, b2, a2, face);
                vec = VecHelper.rotate(vec, -90.0, axis);
                face = Direction.getNearest(vec.x, vec.y, vec.z);
                this.putQuad(ms, builder, a2, b2, b3, a3, face);
                vec = VecHelper.rotate(vec, -90.0, axis);
                face = Direction.getNearest(vec.x, vec.y, vec.z);
                this.putQuad(ms, builder, a3, b3, b4, a4, face);
                vec = VecHelper.rotate(vec, -90.0, axis);
                face = Direction.getNearest(vec.x, vec.y, vec.z);
                this.putQuad(ms, builder, a4, b4, b1, a1, face);
            }
        }
    }

    public void putQuad(PoseStack ms, VertexConsumer builder, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Direction normal) {
        this.putQuadUV(ms, builder, v1, v2, v3, v4, 0.0F, 0.0F, 1.0F, 1.0F, normal);
    }

    public void putQuadUV(PoseStack ms, VertexConsumer builder, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, float minU, float minV, float maxU, float maxV, Direction normal) {
        this.putVertex(ms, builder, v1, minU, minV, normal);
        this.putVertex(ms, builder, v2, maxU, minV, normal);
        this.putVertex(ms, builder, v3, maxU, maxV, normal);
        this.putVertex(ms, builder, v4, minU, maxV, normal);
    }

    protected void putVertex(PoseStack ms, VertexConsumer builder, Vec3 pos, float u, float v, Direction normal) {
        this.putVertex(ms.last(), builder, (float) pos.x, (float) pos.y, (float) pos.z, u, v, normal);
    }

    protected void putVertex(PoseStack.Pose pose, VertexConsumer builder, float x, float y, float z, float u, float v, Direction normal) {
        Color rgb = this.params.rgb;
        if (this.transformNormals == null) {
            this.transformNormals = pose.normal();
        }
        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;
        if (normal != null) {
            xOffset = normal.getStepX();
            yOffset = normal.getStepY();
            zOffset = normal.getStepZ();
        }
        builder.vertex(pose.pose(), x, y, z).color(rgb.getRedAsFloat(), rgb.getGreenAsFloat(), rgb.getBlueAsFloat(), rgb.getAlphaAsFloat() * this.params.alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(this.params.lightMap).normal(pose.normal(), (float) xOffset, (float) yOffset, (float) zOffset).endVertex();
        this.transformNormals = null;
    }

    public static class OutlineParams {

        protected Optional<AllSpecialTextures> faceTexture;

        protected Optional<AllSpecialTextures> hightlightedFaceTexture;

        protected Direction highlightedFace;

        protected boolean fadeLineWidth;

        protected boolean disableCull;

        protected boolean disableNormals;

        protected float alpha;

        protected int lightMap;

        protected Color rgb;

        private float lineWidth;

        public OutlineParams() {
            this.faceTexture = this.hightlightedFaceTexture = Optional.empty();
            this.alpha = 1.0F;
            this.lineWidth = 0.03125F;
            this.fadeLineWidth = true;
            this.rgb = Color.WHITE;
            this.lightMap = 15728880;
        }

        public Outline.OutlineParams colored(int color) {
            this.rgb = new Color(color, false);
            return this;
        }

        public Outline.OutlineParams colored(Color c) {
            this.rgb = c.copy();
            return this;
        }

        public Outline.OutlineParams lightMap(int light) {
            this.lightMap = light;
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

        public Outline.OutlineParams disableNormals() {
            this.disableNormals = true;
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
    }
}