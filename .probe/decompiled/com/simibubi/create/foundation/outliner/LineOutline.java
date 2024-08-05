package com.simibubi.create.foundation.outliner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector4f;

public class LineOutline extends Outline {

    protected final Vector3d start = new Vector3d(0.0, 0.0, 0.0);

    protected final Vector3d end = new Vector3d(0.0, 0.0, 0.0);

    public LineOutline set(Vector3d start, Vector3d end) {
        this.start.set(start.x, start.y, start.z);
        this.end.set(end.x, end.y, end.z);
        return this;
    }

    public LineOutline set(Vec3 start, Vec3 end) {
        this.start.set(start.x, start.y, start.z);
        this.end.set(end.x, end.y, end.z);
        return this;
    }

    @Override
    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, float pt) {
        float width = this.params.getLineWidth();
        if (width != 0.0F) {
            VertexConsumer consumer = buffer.getBuffer(RenderTypes.getOutlineSolid());
            this.params.loadColor(this.colorTemp);
            Vector4f color = this.colorTemp;
            int lightmap = this.params.lightmap;
            boolean disableLineNormals = this.params.disableLineNormals;
            this.renderInner(ms, consumer, camera, pt, width, color, lightmap, disableLineNormals);
        }
    }

    protected void renderInner(PoseStack ms, VertexConsumer consumer, Vec3 camera, float pt, float width, Vector4f color, int lightmap, boolean disableNormals) {
        this.bufferCuboidLine(ms, consumer, camera, this.start, this.end, width, color, lightmap, disableNormals);
    }

    public static class EndChasingLineOutline extends LineOutline {

        private float progress = 0.0F;

        private float prevProgress = 0.0F;

        private boolean lockStart;

        private final Vector3d startTemp = new Vector3d(0.0, 0.0, 0.0);

        public EndChasingLineOutline(boolean lockStart) {
            this.lockStart = lockStart;
        }

        public LineOutline.EndChasingLineOutline setProgress(float progress) {
            this.prevProgress = this.progress;
            this.progress = progress;
            return this;
        }

        @Override
        protected void renderInner(PoseStack ms, VertexConsumer consumer, Vec3 camera, float pt, float width, Vector4f color, int lightmap, boolean disableNormals) {
            float distanceToTarget = Mth.lerp(pt, this.prevProgress, this.progress);
            Vector3d end;
            if (this.lockStart) {
                end = this.start;
            } else {
                end = this.end;
                distanceToTarget = 1.0F - distanceToTarget;
            }
            Vector3d start = this.startTemp;
            double x = (this.start.x - end.x) * (double) distanceToTarget + end.x;
            double y = (this.start.y - end.y) * (double) distanceToTarget + end.y;
            double z = (this.start.z - end.z) * (double) distanceToTarget + end.z;
            start.set((double) ((float) x), (double) ((float) y), (double) ((float) z));
            this.bufferCuboidLine(ms, consumer, camera, start, end, width, color, lightmap, disableNormals);
        }
    }
}