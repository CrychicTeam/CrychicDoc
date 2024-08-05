package noppes.npcs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.model.animation.AnimationHandler;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.JobPuppet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidModel.class })
public class BipedBodyMixin<T extends LivingEntity> {

    @Inject(at = { @At("HEAD") }, method = { "setupAnim" })
    private void setupAnimPre(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel bipedModel = (HumanoidModel) this;
        if (livingEntity instanceof EntityCustomNpc && bipedModel instanceof PlayerModel) {
            EntityCustomNpc playerEntity = (EntityCustomNpc) livingEntity;
            ClientProxy.data = playerEntity.modelData;
            ClientProxy.playerModel = (PlayerModel) bipedModel;
            RenderCustomNpc renderer = (RenderCustomNpc) Minecraft.getInstance().getEntityRenderDispatcher().<T>getRenderer(livingEntity);
            ClientProxy.armorLayer = renderer.armorLayer;
            AnimationHandler.animateBipedPre(ClientProxy.data, bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Inject(at = { @At("TAIL") }, method = { "setupAnim" })
    private void setupAnimPost(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel bipedModel = (HumanoidModel) this;
        if (livingEntity instanceof EntityCustomNpc npc) {
            AnimationHandler.animateBipedPost(ClientProxy.data, bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            if (npc.job.getType() == 9) {
                JobPuppet job = (JobPuppet) npc.job;
                if (job.isActive()) {
                    float pi = (float) Math.PI;
                    float partialTicks = Minecraft.getInstance().getDeltaFrameTime();
                    if (!job.head.disabled) {
                        bipedModel.hat.xRot = bipedModel.head.xRot = job.getRotationX(job.head, job.head2, partialTicks) * pi;
                        bipedModel.hat.yRot = bipedModel.head.yRot = job.getRotationY(job.head, job.head2, partialTicks) * pi;
                        bipedModel.hat.zRot = bipedModel.head.zRot = job.getRotationZ(job.head, job.head2, partialTicks) * pi;
                    }
                    if (!job.body.disabled) {
                        bipedModel.body.xRot = job.getRotationX(job.body, job.body2, partialTicks) * pi;
                        bipedModel.body.yRot = job.getRotationY(job.body, job.body2, partialTicks) * pi;
                        bipedModel.body.zRot = job.getRotationZ(job.body, job.body2, partialTicks) * pi;
                    }
                    if (!job.larm.disabled) {
                        bipedModel.leftArm.xRot = job.getRotationX(job.larm, job.larm2, partialTicks) * pi;
                        bipedModel.leftArm.yRot = job.getRotationY(job.larm, job.larm2, partialTicks) * pi;
                        bipedModel.leftArm.zRot = job.getRotationZ(job.larm, job.larm2, partialTicks) * pi;
                        if (npc.display.getHasLivingAnimation()) {
                            bipedModel.leftArm.zRot = bipedModel.leftArm.zRot - (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
                            bipedModel.leftArm.xRot = bipedModel.leftArm.xRot - Mth.sin(ageInTicks * 0.067F) * 0.05F;
                        }
                    }
                    if (!job.rarm.disabled) {
                        bipedModel.rightArm.xRot = job.getRotationX(job.rarm, job.rarm2, partialTicks) * pi;
                        bipedModel.rightArm.yRot = job.getRotationY(job.rarm, job.rarm2, partialTicks) * pi;
                        bipedModel.rightArm.zRot = job.getRotationZ(job.rarm, job.rarm2, partialTicks) * pi;
                        if (npc.display.getHasLivingAnimation()) {
                            bipedModel.rightArm.zRot = bipedModel.rightArm.zRot + Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                            bipedModel.rightArm.xRot = bipedModel.rightArm.xRot + Mth.sin(ageInTicks * 0.067F) * 0.05F;
                        }
                    }
                    if (!job.rleg.disabled) {
                        bipedModel.rightLeg.xRot = job.getRotationX(job.rleg, job.rleg2, partialTicks) * pi;
                        bipedModel.rightLeg.yRot = job.getRotationY(job.rleg, job.rleg2, partialTicks) * pi;
                        bipedModel.rightLeg.zRot = job.getRotationZ(job.rleg, job.rleg2, partialTicks) * pi;
                    }
                    if (!job.lleg.disabled) {
                        bipedModel.leftLeg.xRot = job.getRotationX(job.lleg, job.lleg2, partialTicks) * pi;
                        bipedModel.leftLeg.yRot = job.getRotationY(job.lleg, job.lleg2, partialTicks) * pi;
                        bipedModel.leftLeg.zRot = job.getRotationZ(job.lleg, job.lleg2, partialTicks) * pi;
                    }
                }
            }
        }
    }
}