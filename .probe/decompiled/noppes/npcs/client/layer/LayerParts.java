package noppes.npcs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.parts.ModelPartWrapper;
import noppes.npcs.client.parts.MpmPart;
import noppes.npcs.client.parts.MpmPartAbstractClient;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.client.parts.PartBehaviorType;
import noppes.npcs.client.parts.PartRenderType;
import noppes.npcs.constants.BodyPart;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.common.util.NopVector3f;

public class LayerParts<T extends EntityCustomNpc, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public LayerParts(LivingEntityRenderer<T, M> render) {
        super(render);
    }

    public void render(PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, EntityCustomNpc player, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
        ModelData data = ModelData.get(player);
        for (MpmPartData part : data.mpmParts) {
            MpmPart mp = part.getPart();
            if (mp != null && mp.renderType != PartRenderType.NONE && mp.isEnabled) {
                MpmPartAbstractClient partc = (MpmPartAbstractClient) mp;
                this.rotate(data, partc, player, (HumanoidModel) this.m_117386_(), limbSwing, limbSwingAmount, partialTicks, age, netHeadYaw, headPitch);
                renderPart(part, partc, mStack, typeBuffer, lightmapUV, player, (HumanoidModel) this.m_117386_(), data);
            }
        }
        data.startMoveAnimation = false;
        data.startAnimation = false;
    }

    public static void renderPart(MpmPartData data, MpmPartAbstractClient partc, PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, EntityCustomNpc player, HumanoidModel model, ModelData pdata) {
        mStack.pushPose();
        boolean shouldRender = true;
        if (partc.bodyPart == BodyPart.HEAD) {
            model.head.translateAndRotate(mStack);
        }
        if (partc.bodyPart == BodyPart.BODY) {
            model.body.translateAndRotate(mStack);
        }
        if (partc.bodyPart == BodyPart.LEGS) {
            ModelPartWrapper rmodelPart = partc.getPart("right_leg");
            ModelPartWrapper lmodelPart = partc.getPart("left_leg");
            if (rmodelPart != null) {
                shouldRender = false;
                mStack.pushPose();
                ModelPartConfig config = pdata.getPartConfig(EnumParts.LEG_RIGHT);
                mStack.translate(0.0F, config.transY * 2.0F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
                if (lmodelPart != null) {
                    lmodelPart.setVisible(false);
                }
                rmodelPart.setVisible(true);
                partc.render(data, mStack, typeBuffer, lightmapUV, player);
                mStack.popPose();
            }
            if (lmodelPart != null) {
                shouldRender = false;
                mStack.pushPose();
                ModelPartConfig config = pdata.getPartConfig(EnumParts.LEG_LEFT);
                mStack.translate(0.0F, config.transY * 2.0F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
                if (rmodelPart != null) {
                    rmodelPart.setVisible(false);
                }
                lmodelPart.setVisible(true);
                partc.render(data, mStack, typeBuffer, lightmapUV, player);
                mStack.popPose();
            }
            if (shouldRender) {
                ModelPartConfig config = pdata.getPartConfig(EnumParts.LEG_LEFT);
                mStack.translate(0.0F, config.transY * 2.0F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
            }
        }
        if (partc.bodyPart == BodyPart.ARMS) {
            ModelPartWrapper rmodelPartx = partc.getPart("right_arm");
            ModelPartWrapper lmodelPartx = partc.getPart("left_arm");
            if (rmodelPartx != null) {
                shouldRender = false;
                mStack.pushPose();
                ModelPartConfig config = pdata.getPartConfig(EnumParts.ARM_RIGHT);
                mStack.translate(0.0F, config.transY + (1.0F - config.scaleY) * 0.125F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
                if (lmodelPartx != null) {
                    lmodelPartx.setVisible(false);
                }
                rmodelPartx.setVisible(true);
                partc.render(data, mStack, typeBuffer, lightmapUV, player);
                mStack.popPose();
            }
            if (lmodelPartx != null) {
                shouldRender = false;
                mStack.pushPose();
                ModelPartConfig config = pdata.getPartConfig(EnumParts.ARM_LEFT);
                mStack.translate(0.0F, config.transY + (1.0F - config.scaleY) * 0.125F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
                if (rmodelPartx != null) {
                    rmodelPartx.setVisible(false);
                }
                lmodelPartx.setVisible(true);
                partc.render(data, mStack, typeBuffer, lightmapUV, player);
                mStack.popPose();
            }
            if (shouldRender) {
                ModelPartConfig config = pdata.getPartConfig(EnumParts.ARM_LEFT);
                mStack.translate(0.0F, config.transY + (1.0F - config.scaleY) * 0.125F, 0.0F);
                mStack.scale(config.scaleX, config.scaleY, config.scaleZ);
            }
        }
        if (shouldRender) {
            partc.render(data, mStack, typeBuffer, lightmapUV, player);
        }
        mStack.popPose();
    }

    private void rotate(ModelData playerdata, MpmPartAbstractClient part, EntityCustomNpc player, HumanoidModel base, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
        part.animationData.animation(19, (int) age, partialTicks);
        int moveAnimation = playerdata.getMoveAnimtion(player);
        if (playerdata.startMoveAnimation) {
            part.animationData.start(moveAnimation);
        }
        boolean didAnimation = false;
        if (playerdata.animation != 0) {
            if (playerdata.startAnimation) {
                part.animationData.start(playerdata.animation);
            }
            didAnimation = part.animationData.animation(playerdata.animation, (int) age, partialTicks);
        }
        if (didAnimation || moveAnimation != 16 && moveAnimation != 18) {
            part.animationData.animation(moveAnimation, Mth.cos(limbSwing * 0.6662F) * limbSwingAmount / 2.0F + 0.5F);
        } else {
            part.animationData.animation(moveAnimation, (int) age, partialTicks);
        }
        if (part.animationType == PartBehaviorType.LEGS) {
            HumanoidModel model = (HumanoidModel) this.m_117386_();
            ModelPartWrapper modelPart = part.getPart("right_leg");
            if (modelPart != null) {
                modelPart.setRot(new NopVector3f(model.rightLeg.xRot, model.rightLeg.yRot, model.rightLeg.zRot));
                modelPart.setPos(new NopVector3f(model.rightLeg.x, model.rightLeg.y, model.rightLeg.z));
            }
            modelPart = part.getPart("left_leg");
            if (modelPart != null) {
                modelPart.setRot(new NopVector3f(model.leftLeg.xRot, model.leftLeg.yRot, model.leftLeg.zRot));
                modelPart.setPos(new NopVector3f(model.leftLeg.x, model.leftLeg.y, model.leftLeg.z));
            }
        }
        if (part.animationType == PartBehaviorType.ARMS) {
            HumanoidModel modelx = (HumanoidModel) this.m_117386_();
            ModelPartWrapper modelPartx = part.getPart("right_arm");
            if (modelPartx != null) {
                modelPartx.setRot(new NopVector3f(modelx.rightArm.xRot, modelx.rightArm.yRot, modelx.rightArm.zRot));
                modelPartx.setPos(new NopVector3f(modelx.rightArm.x, modelx.rightArm.y, modelx.rightArm.z));
            }
            modelPartx = part.getPart("left_arm");
            if (modelPartx != null) {
                modelPartx.setRot(new NopVector3f(modelx.leftArm.xRot, modelx.leftArm.yRot, modelx.leftArm.zRot));
                modelPartx.setPos(new NopVector3f(modelx.leftArm.x, modelx.leftArm.y, modelx.leftArm.z));
            }
        }
        if (part.animationType == PartBehaviorType.BEARD) {
            part.rot = part.rot.set(base.head.xRot < 0.0F ? 0.0F : -base.head.xRot, part.rot.y, part.rot.z);
        }
        if (part.animationType == PartBehaviorType.HAIR) {
            ModelPart head = base.head;
            if (head.xRot < 0.0F) {
                part.rot = part.rot.set(-head.xRot * 1.2F, part.rot.y, part.rot.z);
                if (head.xRot > -1.0F) {
                    part.pos = part.pos.set(part.pos.x, -head.xRot * 1.5F, -head.xRot * 1.5F);
                }
            } else {
                part.pos = NopVector3f.ZERO;
            }
        }
        if (part.animationType == PartBehaviorType.WINGS) {
            ModelPartWrapper modelPartxx = part.getPart("right_wing");
            ModelPartWrapper modelPartL = part.getPart("left_wing");
            float xRot;
            float zRot;
            if (player.m_9236_().m_46859_(player.m_20183_().below())) {
                float motion = Math.abs(Mth.sin(limbSwing * 0.033F + (float) Math.PI) * 0.4F) * limbSwingAmount;
                float speed = 0.55F + 0.5F * motion;
                float y = Mth.sin(age * 0.35F);
                xRot = zRot = y * 0.5F * speed;
            } else {
                zRot = Mth.cos(age * 0.09F) * 0.05F + 0.05F;
                xRot = Mth.sin(age * 0.067F) * 0.05F;
            }
            modelPartxx.setRot(modelPartxx.oriRot.add(xRot, xRot, zRot));
            modelPartL.setRot(modelPartL.oriRot.add(xRot, -xRot, -zRot));
        }
        if (part.animationType == PartBehaviorType.WINGS2) {
            ModelPartWrapper modelPartxx = part.getPart("right_wing");
            ModelPartWrapper modelPartL = part.getPart("left_wing");
            float yRot;
            if (player.m_9236_().m_46859_(player.m_20183_().below())) {
                float motion = Math.abs(Mth.sin(limbSwing * 0.033F + (float) Math.PI) * 0.4F) * limbSwingAmount;
                float speed = 0.55F + 0.5F * motion;
                float y = Mth.sin(age * 0.35F);
                yRot = y * 0.5F * speed;
            } else {
                yRot = Mth.sin(age * 0.07F) * 0.44F;
            }
            modelPartxx.setRot(modelPartxx.oriRot.add(0.0F, yRot, 0.0F));
            modelPartL.setRot(modelPartL.oriRot.add(0.0F, -yRot, 0.0F));
        }
    }
}