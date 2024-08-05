package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelMurmurNeck;
import com.github.alexthe666.alexsmobs.client.model.ModelTendonClaw;
import com.github.alexthe666.alexsmobs.entity.EntityTendonSegment;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RenderTendonSegment extends EntityRenderer<EntityTendonSegment> {

    private static final ResourceLocation CLAW_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/tendon_whip_claw.png");

    private static final ModelTendonClaw CLAW_MODEL = new ModelTendonClaw();

    public RenderTendonSegment(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public boolean shouldRender(EntityTendonSegment entity, Frustum frustum, double x, double y, double z) {
        Entity next = entity.getFromEntity();
        return next != null && frustum.isVisible(entity.m_20191_().minmax(next.getBoundingBox())) || super.shouldRender(entity, frustum, x, y, z);
    }

    public void render(EntityTendonSegment entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
        poseStack.pushPose();
        Entity fromEntity = entity.getFromEntity();
        float x = (float) Mth.lerp((double) partialTicks, entity.f_19854_, entity.m_20185_());
        float y = (float) Mth.lerp((double) partialTicks, entity.f_19855_, entity.m_20186_());
        float z = (float) Mth.lerp((double) partialTicks, entity.f_19856_, entity.m_20189_());
        if (fromEntity != null) {
            float progress = (entity.prevProgress + (entity.getProgress() - entity.prevProgress) * partialTicks) / 3.0F;
            Vec3 distVec = this.getPositionOfPriorMob(entity, fromEntity, partialTicks).subtract((double) x, (double) y, (double) z);
            Vec3 to = distVec.scale((double) (1.0F - progress));
            Vec3 from = distVec;
            int segmentCount = 0;
            Vec3 currentNeckButt = distVec;
            VertexConsumer neckConsumer;
            if (entity.hasGlint()) {
                neckConsumer = AMRenderTypes.createMergedVertexConsumer(buffer.getBuffer(AMRenderTypes.m_110499_()), buffer.getBuffer(RenderType.entityCutoutNoCull(RenderMurmurBody.TEXTURE)));
            } else {
                neckConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(RenderMurmurBody.TEXTURE));
            }
            ModelMurmurNeck.THIN = true;
            for (double remainingDistance = to.distanceTo(distVec); segmentCount < 128 && remainingDistance > 0.0; segmentCount++) {
                remainingDistance = Math.min(from.distanceTo(to), 0.5);
                Vec3 linearVec = to.subtract(currentNeckButt);
                Vec3 powVec = new Vec3(this.modifyVecAngle(linearVec.x), this.modifyVecAngle(linearVec.y), this.modifyVecAngle(linearVec.z));
                Vec3 next = powVec.normalize().scale(remainingDistance).add(currentNeckButt);
                int neckLight = this.getLightColor(entity, to.add(currentNeckButt).add((double) x, (double) y, (double) z));
                RenderMurmurHead.renderNeckCube(currentNeckButt, next, poseStack, neckConsumer, neckLight, OverlayTexture.NO_OVERLAY, 0.0F);
                currentNeckButt = next;
            }
            ModelMurmurNeck.THIN = false;
            VertexConsumer clawConsumer;
            if (entity.hasGlint()) {
                clawConsumer = AMRenderTypes.createMergedVertexConsumer(buffer.getBuffer(AMRenderTypes.m_110499_()), buffer.getBuffer(RenderType.entityCutoutNoCull(CLAW_TEXTURE)));
            } else {
                clawConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(CLAW_TEXTURE));
            }
            if (entity.hasClaw() || entity.isRetracting()) {
                poseStack.pushPose();
                poseStack.translate(to.x, to.y, to.z);
                float rotY = (float) (Mth.atan2(to.x, to.z) * 180.0F / (float) Math.PI);
                float rotX = (float) (-(Mth.atan2(to.y, to.horizontalDistance()) * 180.0F / (float) Math.PI));
                CLAW_MODEL.setAttributes(rotX, rotY, 1.0F - progress);
                CLAW_MODEL.m_7695_(poseStack, clawConsumer, this.getLightColor(entity, to.add((double) x, (double) y, (double) z)), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

    private Vec3 getPositionOfPriorMob(EntityTendonSegment segment, Entity mob, float partialTicks) {
        double d4 = Mth.lerp((double) partialTicks, mob.xo, mob.getX());
        double d5 = Mth.lerp((double) partialTicks, mob.yo, mob.getY());
        double d6 = Mth.lerp((double) partialTicks, mob.zo, mob.getZ());
        float f3 = 0.0F;
        if (mob instanceof Player && segment.isCreator(mob)) {
            Player player = (Player) mob;
            float f = player.m_21324_(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(partialTicks, player.f_20884_, player.f_20883_) * (float) (Math.PI / 180.0);
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            double d0 = (double) Mth.sin(f2);
            double d1 = (double) Mth.cos(f2);
            double d2 = (double) i * 0.35;
            ItemStack itemstack = player.m_21205_();
            if (!itemstack.is(AMItemRegistry.TENDON_WHIP.get())) {
                i = -i;
            }
            if ((this.f_114476_.options == null || this.f_114476_.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
                double d7 = 960.0 / (double) this.f_114476_.options.fov().get().intValue();
                Vec3 vec3 = this.f_114476_.camera.getNearPlane().getPointOnPlane((float) i * 0.6F, -1.0F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.25F);
                vec3 = vec3.xRot(-f1 * 0.35F);
                d4 = Mth.lerp((double) partialTicks, player.f_19854_, player.m_20185_()) + vec3.x;
                d5 = Mth.lerp((double) partialTicks, player.f_19855_, player.m_20186_()) + vec3.y;
                d6 = Mth.lerp((double) partialTicks, player.f_19856_, player.m_20189_()) + vec3.z;
                f3 = player.m_20192_() * 0.4F;
            } else {
                d4 = Mth.lerp((double) partialTicks, player.f_19854_, player.m_20185_()) - d1 * d2 - d0 * 0.2;
                d5 = player.f_19855_ + (double) player.m_20192_() + (player.m_20186_() - player.f_19855_) * (double) partialTicks - 1.0;
                d6 = Mth.lerp((double) partialTicks, player.f_19856_, player.m_20189_()) - d0 * d2 + d1 * 0.2;
                f3 = (player.m_6047_() ? -0.1875F : 0.0F) - player.m_20192_() * 0.3F;
            }
        }
        return new Vec3(d4, d5 + (double) f3, d6);
    }

    private double modifyVecAngle(double dimension) {
        float abs = (float) Math.abs(dimension);
        return Math.signum(dimension) * Mth.clamp(Math.pow((double) abs, 0.1), 0.05 * (double) abs, (double) abs);
    }

    private int getLightColor(Entity head, Vec3 vec3) {
        BlockPos blockpos = AMBlockPos.fromVec3(vec3);
        if (head.level().m_46805_(blockpos)) {
            int i = LevelRenderer.getLightColor(head.level(), blockpos);
            int j = LevelRenderer.getLightColor(head.level(), blockpos.above());
            int k = i & 0xFF;
            int l = j & 0xFF;
            int i1 = i >> 16 & 0xFF;
            int j1 = j >> 16 & 0xFF;
            return Math.max(k, l) | Math.max(i1, j1) << 16;
        } else {
            return 0;
        }
    }

    public ResourceLocation getTextureLocation(EntityTendonSegment entity) {
        return null;
    }
}