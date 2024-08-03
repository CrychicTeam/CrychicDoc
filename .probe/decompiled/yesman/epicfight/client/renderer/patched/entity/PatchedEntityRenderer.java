package yesman.epicfight.client.renderer.patched.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

@OnlyIn(Dist.CLIENT)
public abstract class PatchedEntityRenderer<E extends Entity, T extends EntityPatch<E>, R extends EntityRenderer<E>, AM extends AnimatedMesh> {

    protected static Method shouldShowName = ObfuscationReflectionHelper.findMethod(EntityRenderer.class, "m_6512_", new Class[] { Entity.class });

    protected static Method renderNameTag = ObfuscationReflectionHelper.findMethod(EntityRenderer.class, "m_7649_", new Class[] { Entity.class, Component.class, PoseStack.class, MultiBufferSource.class, int.class });

    public void render(E entityIn, T entitypatch, R renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        try {
            RenderNameTagEvent renderNameplateEvent = new RenderNameTagEvent(entityIn, entityIn.getDisplayName(), renderer, poseStack, buffer, packedLight, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
            if (((Boolean) shouldShowName.invoke(renderer, entityIn) || renderNameplateEvent.getResult() == Result.ALLOW) && renderNameplateEvent.getResult() != Result.DENY) {
                renderNameTag.invoke(renderer, entityIn, renderNameplateEvent.getContent(), poseStack, buffer, packedLight);
            }
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var9) {
            var9.printStackTrace();
        }
    }

    protected void setJointTransform(String jointName, Armature modelArmature, OpenMatrix4f mat) {
        Joint joint = modelArmature.searchJointByName(jointName);
        if (joint != null) {
            joint.getPoseTransform().mulFront(mat);
        }
    }

    public void mulPoseStack(PoseStack poseStack, Armature armature, E entityIn, T entitypatch, float partialTicks) {
        OpenMatrix4f modelMatrix = entitypatch.getModelMatrix(partialTicks);
        OpenMatrix4f transpose = modelMatrix.transpose(null);
        poseStack.mulPose(QuaternionUtils.YP.rotationDegrees(180.0F));
        MathUtils.translateStack(poseStack, modelMatrix);
        MathUtils.rotateStack(poseStack, transpose);
        MathUtils.scaleStack(poseStack, transpose);
        if (entitypatch.getOriginal() instanceof LivingEntity livingEntity && LivingEntityRenderer.isEntityUpsideDown(livingEntity)) {
            poseStack.translate(0.0, (double) (livingEntity.m_20206_() + 0.1F), 0.0);
            poseStack.mulPose(QuaternionUtils.ZP.rotationDegrees(180.0F));
        }
    }

    public OpenMatrix4f[] getPoseMatrices(T entitypatch, Armature armature, float partialTicks) {
        armature.initializeTransform();
        this.setJointTransforms(entitypatch, armature, partialTicks);
        return armature.getAllPoseTransform(partialTicks);
    }

    public abstract AM getMesh(T var1);

    protected void setJointTransforms(T entitypatch, Armature armature, float partialTicks) {
    }
}