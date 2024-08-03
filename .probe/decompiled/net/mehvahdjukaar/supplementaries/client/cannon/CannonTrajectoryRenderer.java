package net.mehvahdjukaar.supplementaries.client.cannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.ModRenderTypes;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CannonTrajectoryRenderer {

    public static void render(CannonBlockTile blockEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float partialTicks, float yaw) {
        if (CannonController.cannon == blockEntity) {
            if (CannonController.hit != null && CannonController.trajectory != null) {
                boolean rendersRed = !blockEntity.readyToFire();
                BlockPos cannonPos = blockEntity.m_58899_();
                Minecraft mc = Minecraft.getInstance();
                boolean debug = !mc.showOnlyReducedInfo() && mc.getEntityRenderDispatcher().shouldRenderHitBoxes();
                poseStack.pushPose();
                Vec3 targetVector = CannonController.hit.getLocation().subtract(cannonPos.getCenter());
                Vec2 target = new Vec2((float) Mth.length(targetVector.x, targetVector.z), (float) targetVector.y);
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Axis.YP.rotation(-yaw));
                if (debug) {
                    renderTargetLine(poseStack, buffer, target);
                }
                boolean hitAir = mc.level.m_8055_(CannonController.trajectory.getHitPos(cannonPos, yaw)).m_60795_();
                renderArrows(poseStack, buffer, partialTicks, CannonController.trajectory, hitAir, rendersRed);
                poseStack.popPose();
                if (!hitAir) {
                    renderTargetCircle(poseStack, buffer, yaw, rendersRed);
                }
                if (debug && CannonController.hit instanceof BlockHitResult bh) {
                    renderTargetBlock(poseStack, buffer, cannonPos, bh);
                }
            }
        }
    }

    private static void renderTargetBlock(PoseStack poseStack, MultiBufferSource buffer, BlockPos pos, BlockHitResult bh) {
        poseStack.pushPose();
        BlockPos targetPos = bh.getBlockPos();
        VertexConsumer lines = buffer.getBuffer(RenderType.lines());
        Vec3 distance1 = targetPos.getCenter().subtract(pos.getCenter());
        AABB bb = new AABB(distance1, distance1.add(1.0, 1.0, 1.0)).inflate(0.01);
        LevelRenderer.renderLineBox(poseStack, lines, bb, 1.0F, 0.0F, 0.0F, 1.0F);
        poseStack.popPose();
    }

    private static void renderTargetCircle(PoseStack poseStack, MultiBufferSource buffer, float yaw, boolean red) {
        poseStack.pushPose();
        Material circleMaterial = red ? ModMaterials.CANNON_TARGET_RED_MATERIAL : ModMaterials.CANNON_TARGET_MATERIAL;
        VertexConsumer circleBuilder = circleMaterial.buffer(buffer, RenderType::m_110452_);
        Vec3 targetVec = new Vec3(0.0, (double) CannonController.trajectory.point().y, (double) (-CannonController.trajectory.point().x)).yRot(-yaw);
        poseStack.translate(targetVec.x + 0.5, targetVec.y + 0.5 + 0.05, targetVec.z + 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        int lu = 240;
        int lv = 15728640;
        VertexUtil.addQuad(circleBuilder, poseStack, -2.0F, -2.0F, 2.0F, 2.0F, lu, lv);
        poseStack.popPose();
    }

    private static void renderTargetLine(PoseStack poseStack, MultiBufferSource buffer, Vec2 target) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        consumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 0, 0, 255).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, 0.0F, target.y, -target.x).color(255, 0, 0, 255).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, 0.01F, target.y, -target.x).color(255, 0, 0, 255).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, 0.01F, 0.0F, 0.0F).color(255, 0, 0, 255).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    }

    private static void renderArrows(PoseStack poseStack, MultiBufferSource buffer, float partialTicks, CannonTrajectory trajectory, boolean hitAir, boolean red) {
        float finalTime = (float) trajectory.finalTime();
        if (finalTime > 100000.0F) {
            Supplementaries.error();
        } else {
            poseStack.pushPose();
            float scale = 1.0F;
            float size = 0.15625F * scale;
            VertexConsumer consumer = buffer.getBuffer(red ? ModRenderTypes.CANNON_TRAJECTORY_RED : ModRenderTypes.CANNON_TRAJECTORY);
            Matrix4f matrix = poseStack.last().pose();
            float py = 0.0F;
            float px = 0.0F;
            float d = (float) (-(System.currentTimeMillis() % 1000L)) / 1000.0F;
            float step = finalTime / (float) ((int) finalTime);
            float maxT = finalTime + (hitAir ? 0.0F : step);
            for (float t = step; t < maxT; t += step) {
                float textureStart = d % 1.0F;
                consumer.vertex(matrix, -size, py, px).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, textureStart).endVertex();
                consumer.vertex(matrix, size, py, px).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.3125F, textureStart).endVertex();
                double ny = trajectory.getY((double) t);
                double nx = -trajectory.getX((double) t);
                float dis = (float) Mth.length(nx - (double) px, ny - (double) py) / scale;
                float textEnd = textureStart + dis;
                d += dis;
                py = (float) ny;
                px = (float) nx;
                int alpha = t + step >= maxT ? 0 : 1;
                consumer.vertex(matrix, size, py, px).color(1.0F, 1.0F, 1.0F, (float) alpha).uv(0.3125F, textEnd).endVertex();
                consumer.vertex(matrix, -size, py, px).color(1.0F, 1.0F, 1.0F, (float) alpha).uv(0.0F, textEnd).endVertex();
            }
            poseStack.popPose();
        }
    }
}