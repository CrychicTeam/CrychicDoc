package journeymap.client.render.ingame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import journeymap.client.render.draw.MatrixDrawUtil;
import journeymap.client.waypoint.Waypoint;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WaypointBeaconRenderer extends WaypointRenderer {

    @Override
    protected void render(PoseStack poseStack, MultiBufferSource buffers, Waypoint waypoint, float partialTicks, long gameTime, float[] rgba, float fadeAlpha, double shiftX, double shiftY, double shiftZ, Vec3 playerVec, Vec3 waypointVec, double viewDistance, double actualDistance, double scale) {
        boolean showStaticInnerBeam = this.waypointProperties.showStaticBeam.get();
        boolean showRotatingOuterBeam = this.waypointProperties.showRotatingBeam.get();
        if (showStaticInnerBeam || showRotatingOuterBeam) {
            poseStack.pushPose();
            poseStack.translate(shiftX, -180.0, shiftZ);
            this.renderBeamSegment(poseStack, buffers, partialTicks, gameTime, 1, 360, rgba, 0.2F, 0.25F, showStaticInnerBeam, showRotatingOuterBeam);
            poseStack.popPose();
        }
    }

    public void renderBeamSegment(PoseStack poseStack, MultiBufferSource buffer, float partialTicks, long gameTime, int yOffset, int height, float[] colors, float beamRadius, float glowRadius, boolean showStaticInnerBeam, boolean showRotatingOuterBeam) {
        float texScale = 1.0F;
        int heightOffset = yOffset + height;
        float rotation = (float) Math.floorMod(gameTime, 40L) + partialTicks;
        float texOffset = -((float) (-gameTime) * 0.2F - (float) Mth.floor((float) (-gameTime) * 0.1F)) * 0.6F;
        float red = colors[0];
        float blue = colors[1];
        float green = colors[2];
        float alpha = colors[3];
        VertexConsumer beamBuffer = buffer.getBuffer(WaypointRenderTypes.BEAM_RENDER_TYPE);
        poseStack.pushPose();
        if (!showStaticInnerBeam) {
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 45.0F));
        }
        float V2 = -1.0F + texOffset;
        float innerV1 = (float) height * texScale * (0.5F / beamRadius) + V2;
        this.renderPart(poseStack, beamBuffer, red, blue, green, alpha, yOffset, heightOffset, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius, 0.0F, 1.0F, innerV1, V2);
        poseStack.popPose();
        float outerV1 = (float) height * texScale + V2;
        poseStack.pushPose();
        if (showRotatingOuterBeam) {
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 45.0F));
        }
        this.renderPart(poseStack, beamBuffer, red, blue, green, alpha, yOffset, heightOffset, -glowRadius, -glowRadius, glowRadius, -glowRadius, -glowRadius, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, outerV1, V2);
        poseStack.popPose();
    }

    private void renderPart(PoseStack poseStack, VertexConsumer buffer, float red, float green, float blue, float alpha, int yMin, int yMax, float p1, float p2, float p3, float p4, float p5, float p6, float p7, float p8, float u1, float u2, float v1, float v2) {
        PoseStack.Pose entry = poseStack.last();
        Matrix4f matrixPos = entry.pose();
        Matrix3f normal = entry.normal();
        this.addQuad(matrixPos, normal, buffer, red, green, blue, alpha, yMin, yMax, p1, p2, p3, p4, u1, u2, v1, v2);
        this.addQuad(matrixPos, normal, buffer, red, green, blue, alpha, yMin, yMax, p7, p8, p5, p6, u1, u2, v1, v2);
        this.addQuad(matrixPos, normal, buffer, red, green, blue, alpha, yMin, yMax, p3, p4, p7, p8, u1, u2, v1, v2);
        this.addQuad(matrixPos, normal, buffer, red, green, blue, alpha, yMin, yMax, p5, p6, p1, p2, u1, u2, v1, v2);
    }

    private void addQuad(Matrix4f matrixPos, Matrix3f normal, VertexConsumer bufferIn, float red, float green, float blue, float alpha, int yMin, int yMax, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        MatrixDrawUtil.addVertexUV(matrixPos, normal, bufferIn, red, green, blue, alpha, yMax, x1, z1, u2, v1);
        MatrixDrawUtil.addVertexUV(matrixPos, normal, bufferIn, red, green, blue, alpha, yMin, x1, z1, u2, v2);
        MatrixDrawUtil.addVertexUV(matrixPos, normal, bufferIn, red, green, blue, alpha, yMin, x2, z2, u1, v2);
        MatrixDrawUtil.addVertexUV(matrixPos, normal, bufferIn, red, green, blue, alpha, yMax, x2, z2, u1, v1);
    }
}