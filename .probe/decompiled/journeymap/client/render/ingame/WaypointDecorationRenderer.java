package journeymap.client.render.ingame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import journeymap.client.Constants;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.draw.MatrixDrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.waypoint.Waypoint;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class WaypointDecorationRenderer extends WaypointRenderer {

    @Override
    protected void render(PoseStack poseStack, MultiBufferSource buffers, Waypoint waypoint, float partialTicks, long gameTime, float[] rgba, float fadeAlpha, double shiftX, double shiftY, double shiftZ, Vec3 playerVec, Vec3 waypointVec, double viewDistance, double actualDistance, double scale) {
        if (this.lookingAtBeacon(waypointVec) && viewDistance > 0.5) {
            poseStack.pushPose();
            this.renderNameTag(waypoint, poseStack, buffers, fadeAlpha, scale, actualDistance, shiftX, shiftY, shiftZ);
            poseStack.popPose();
        }
        if (this.lookingAtBeacon(waypointVec) && viewDistance > 0.5 && waypoint.showDeviation() && this.waypointProperties.showDeviationLabel.get()) {
            poseStack.pushPose();
            this.renderDeviation(waypoint, poseStack, buffers, fadeAlpha, scale, shiftX, shiftY, shiftZ, playerVec, waypointVec);
            poseStack.popPose();
        }
        if (viewDistance > 0.1 && this.waypointProperties.showTexture.get()) {
            poseStack.pushPose();
            this.renderIcon(waypoint, poseStack, buffers, fadeAlpha, scale, shiftX, shiftY, shiftZ);
            poseStack.popPose();
        }
    }

    protected void renderIcon(Waypoint waypoint, PoseStack poseStack, MultiBufferSource buffers, float alpha, double scale, double shiftX, double shiftY, double shiftZ) {
        scale *= (double) (this.waypointProperties.textureSmall.get() ? 2 : 4);
        poseStack.pushPose();
        poseStack.translate(shiftX, shiftY, shiftZ);
        poseStack.mulPose(this.renderManager.camera.rotation());
        poseStack.scale((float) (-scale), (float) (-scale), (float) scale);
        Texture texture = TextureCache.getWaypointIcon(waypoint.getTextureResource());
        ResourceLocation location = texture.getLocation() == null ? waypoint.getTextureResource() : texture.getLocation();
        RenderType type = WaypointRenderTypes.getIcon(texture);
        VertexConsumer vertexBuilder = buffers.getBuffer(type);
        RenderWrapper.setShader(GameRenderer::m_172814_);
        RenderWrapper.activeTexture(33984);
        RenderWrapper.bindTexture(texture.getTextureId());
        RenderWrapper.setShaderTexture(0, location);
        MatrixDrawUtil.drawColoredImage(waypoint.getTexture(), poseStack, vertexBuilder, waypoint.getIconColor(), alpha, (double) (-(waypoint.getTexture().getWidth() >> 1)) + 0.5, (double) (-(waypoint.getTexture().getHeight() >> 1)) + 0.2, 0.0);
        poseStack.popPose();
    }

    protected void renderWaypointLabel(String label, Waypoint waypoint, double labelY, PoseStack poseStack, MultiBufferSource buffers, float alpha, double scale, double shiftX, double shiftY, double shiftZ) {
        poseStack.pushPose();
        poseStack.translate(shiftX, shiftY, shiftZ);
        poseStack.mulPose(this.renderManager.camera.rotation());
        poseStack.scale((float) (-scale), (float) (-scale), (float) scale);
        float fontScale = this.waypointProperties.fontScale.get();
        DrawUtil.drawBatchLabel(poseStack, Component.literal(label), buffers, 1.0, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Above, -16777216, 0.6F * alpha, waypoint.getSafeColor(), alpha, (double) fontScale, false);
        DrawUtil.drawBatchLabel(poseStack, Component.literal(label), buffers, 1.0, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Above, -16777216, 0.4F * alpha, waypoint.getSafeColor(), alpha, (double) fontScale, false);
        poseStack.popPose();
    }

    protected void renderNameTag(Waypoint waypoint, PoseStack poseStack, MultiBufferSource buffers, float alpha, double scale, double actualDistance, double shiftX, double shiftY, double shiftZ) {
        String distanceLabel = Constants.getString("jm.waypoint.distance_meters", "%1.0f");
        String label = waypoint.getName();
        boolean showName = this.waypointProperties.showName.get() && label != null && label.length() > 0;
        boolean showDistance = this.waypointProperties.showDistance.get();
        if (showName || showDistance) {
            StringBuilder sb = new StringBuilder();
            if (this.waypointProperties.boldLabel.get()) {
                sb.append(ChatFormatting.BOLD);
            }
            if (showName) {
                sb.append(label);
            }
            if (showName && showDistance) {
                sb.append(" ");
            }
            if (showDistance) {
                sb.append(String.format(distanceLabel, actualDistance));
            }
            if (sb.length() > 0) {
                label = sb.toString();
                double labelY = (double) (-(waypoint.getTexture().getHeight() >> 1) - 8);
                this.renderWaypointLabel(label, waypoint, labelY, poseStack, buffers, alpha, scale, shiftX, shiftY, shiftZ);
            }
        }
    }

    protected void renderDeviation(Waypoint waypoint, PoseStack poseStack, MultiBufferSource buffers, float alpha, double scale, double shiftX, double shiftY, double shiftZ, Vec3 playerVec, Vec3 waypointVec) {
        StringBuilder sb = new StringBuilder();
        Vec3 vecTo = playerVec.vectorTo(waypointVec);
        if (this.waypointProperties.boldLabel.get()) {
            sb.append(ChatFormatting.BOLD);
        }
        sb.append(String.format("x:%d, y:%d, z:%d", (int) vecTo.x, (int) vecTo.y, (int) vecTo.z));
        double labelY = (double) ((waypoint.getTexture().getHeight() >> 1) + 35);
        this.renderWaypointLabel(sb.toString(), waypoint, labelY, poseStack, buffers, alpha, scale, shiftX, shiftY, shiftZ);
    }

    protected boolean lookingAtBeacon(Vec3 waypointVec) {
        if (this.waypointProperties.autoHideLabel.get()) {
            int angle = 5;
            double yaw = Math.atan2(this.renderManager.camera.getPosition().z() - waypointVec.z, this.renderManager.camera.getPosition().x() - waypointVec.x);
            double degrees = Math.toDegrees(yaw) + 90.0;
            if (degrees < 0.0) {
                degrees += 360.0;
            }
            double playerYaw = (double) (Minecraft.getInstance().cameraEntity.getYHeadRot() % 360.0F);
            if (playerYaw < 0.0) {
                playerYaw += 360.0;
            }
            playerYaw = Math.toRadians(playerYaw);
            double playerDegrees = Math.toDegrees(playerYaw);
            degrees += (double) angle;
            playerDegrees += (double) angle;
            return !(Math.abs(degrees + (double) angle - (playerDegrees + (double) angle)) > (double) angle);
        } else {
            return true;
        }
    }
}