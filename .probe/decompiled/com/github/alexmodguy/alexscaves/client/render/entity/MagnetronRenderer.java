package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.MagnetronModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronPartEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetronJoint;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class MagnetronRenderer extends MobRenderer<MagnetronEntity, MagnetronModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/magnetron.png");

    private static final ResourceLocation TEXTURE_GLOW_RED = new ResourceLocation("alexscaves:textures/entity/magnetron_glow_red.png");

    private static final ResourceLocation TEXTURE_GLOW_BLUE = new ResourceLocation("alexscaves:textures/entity/magnetron_glow_blue.png");

    private static final ResourceLocation TEXTURE_GLOW_EYES = new ResourceLocation("alexscaves:textures/entity/magnetron_glow_eyes.png");

    private Map<UUID, LightningRender> lightningRenderMap = new HashMap();

    public MagnetronRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MagnetronModel(), 0.8F);
        this.m_115326_(new MagnetronRenderer.LayerGlow());
    }

    public void render(MagnetronEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        double x = Mth.lerp((double) partialTicks, entityIn.f_19790_, entityIn.m_20185_());
        double y = Mth.lerp((double) partialTicks, entityIn.f_19791_, entityIn.m_20186_());
        double z = Mth.lerp((double) partialTicks, entityIn.f_19792_, entityIn.m_20189_());
        float yaw = entityIn.f_20884_ + (entityIn.f_20883_ - entityIn.f_20884_) * partialTicks;
        LightningRender lightningRender = this.getLightingRender(entityIn.m_20148_());
        poseStack.pushPose();
        if (entityIn.isFunctionallyMultipart() && entityIn.allParts.length > 0) {
            Entity rShoulder = entityIn;
            Entity lShoulder = entityIn;
            Entity rElbow = entityIn;
            Entity lElbow = entityIn;
            Entity rHand = entityIn;
            Entity lHand = entityIn;
            Entity rKnee = entityIn;
            Entity lKnee = entityIn;
            Entity rFoot = entityIn;
            Entity lFoot = entityIn;
            for (MagnetronPartEntity part : entityIn.allParts) {
                double partX = Mth.lerp((double) partialTicks, part.f_19790_, part.m_20185_()) - x;
                double partY = Mth.lerp((double) partialTicks, part.f_19791_, part.m_20186_()) - y;
                double partZ = Mth.lerp((double) partialTicks, part.f_19792_, part.m_20189_()) - z;
                BlockState blockstate = part.getVisualBlockState();
                if (blockstate != null) {
                    poseStack.pushPose();
                    poseStack.translate(partX, partY, partZ);
                    poseStack.translate(-0.5, 0.0, -0.5);
                    if (blockstate.m_61138_(HorizontalDirectionalBlock.FACING)) {
                        blockstate = (BlockState) blockstate.m_61124_(HorizontalDirectionalBlock.FACING, Direction.NORTH);
                    }
                    if (blockstate.m_61138_(BlockStateProperties.FACING)) {
                        blockstate = (BlockState) blockstate.m_61124_(BlockStateProperties.FACING, Direction.DOWN);
                    }
                    if (blockstate.m_61138_(HorizontalDirectionalBlock.FACING)) {
                        poseStack.translate(0.5, 0.5, 0.5);
                        poseStack.mulPose(Axis.YP.rotationDegrees(-((Direction) blockstate.m_61143_(HorizontalDirectionalBlock.FACING)).toYRot()));
                        poseStack.translate(-0.5, -0.5, -0.5);
                    }
                    if (blockstate.m_204336_(ACTagRegistry.MAGNETRON_WEAPONS) && part.getJoint() == MagnetronJoint.HAND) {
                        poseStack.translate(0.5, 0.5, 0.5);
                        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
                        float poseProgress = entityIn.getAttackPoseProgress(1.0F);
                        float priorPoseProgress = 1.0F - poseProgress;
                        this.rotateHandFor(poseStack, part, entityIn.getAttackPose(), poseProgress);
                        this.rotateHandFor(poseStack, part, entityIn.getPrevAttackPose(), priorPoseProgress);
                        poseStack.translate(-0.5, -0.5, -0.5);
                    }
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
                if (part.getJoint() == MagnetronJoint.SHOULDER) {
                    if (part.left) {
                        lShoulder = part;
                    } else {
                        rShoulder = part;
                    }
                } else if (part.getJoint() == MagnetronJoint.ELBOW) {
                    if (part.left) {
                        lElbow = part;
                    } else {
                        rElbow = part;
                    }
                } else if (part.getJoint() == MagnetronJoint.HAND) {
                    if (part.left) {
                        lHand = part;
                    } else {
                        rHand = part;
                    }
                } else if (part.getJoint() == MagnetronJoint.KNEE) {
                    if (part.left) {
                        lKnee = part;
                    } else {
                        rKnee = part;
                    }
                } else if (part.getJoint() == MagnetronJoint.FOOT) {
                    if (part.left) {
                        lFoot = part;
                    } else {
                        rFoot = part;
                    }
                }
            }
            Vector4f red = new Vector4f(0.9F, 0.2F, 0.2F, 1.0F);
            Vector4f blue = new Vector4f(0.2F, 0.2F, 0.8F, 1.0F);
            this.spawnLightningBetweenEntities(entityIn, rShoulder, 0.5F, lightningRender, blue, partialTicks, entityIn.lightningAnimOffsets[0]);
            this.spawnLightningBetweenEntities(entityIn, lShoulder, 0.5F, lightningRender, red, partialTicks, entityIn.lightningAnimOffsets[1]);
            this.spawnLightningBetweenEntities(rShoulder, rElbow, 0.5F, lightningRender, blue, partialTicks, Vec3.ZERO);
            this.spawnLightningBetweenEntities(rElbow, rHand, 0.0F, lightningRender, blue, partialTicks, Vec3.ZERO);
            this.spawnLightningBetweenEntities(lShoulder, lElbow, 0.0F, lightningRender, red, partialTicks, Vec3.ZERO);
            this.spawnLightningBetweenEntities(lElbow, lHand, 0.0F, lightningRender, red, partialTicks, Vec3.ZERO);
            this.spawnLightningBetweenEntities(entityIn, rKnee, 0.5F, lightningRender, blue, partialTicks, entityIn.lightningAnimOffsets[2]);
            this.spawnLightningBetweenEntities(entityIn, lKnee, 0.5F, lightningRender, red, partialTicks, entityIn.lightningAnimOffsets[3]);
            this.spawnLightningBetweenEntities(rKnee, rFoot, 0.0F, lightningRender, blue, partialTicks, Vec3.ZERO);
            this.spawnLightningBetweenEntities(lKnee, lFoot, 0.0F, lightningRender, red, partialTicks, Vec3.ZERO);
            Vec3 lightningHeadFrom = entityIn.m_20318_(partialTicks).add(0.0, 0.5, 0.0);
            float yawRadian = (float) Math.toRadians((double) (-yaw));
            Vec3 lHeadFrom = lightningHeadFrom.add(new Vec3(0.2, 1.0, 0.0).yRot(yawRadian));
            Vec3 rHeadFrom = lightningHeadFrom.add(new Vec3(-0.2, 1.0, 0.0).yRot(yawRadian));
            this.spawnLightningBetweenVecs(entityIn, lightningHeadFrom.add(entityIn.lightningAnimOffsets[4]), lHeadFrom, lightningRender, blue, partialTicks);
            this.spawnLightningBetweenVecs(entityIn, lightningHeadFrom.add(entityIn.lightningAnimOffsets[5]), rHeadFrom, lightningRender, red, partialTicks);
        }
        poseStack.pushPose();
        poseStack.translate(-x, -y, -z);
        lightningRender.render(partialTicks, poseStack, bufferIn);
        poseStack.popPose();
        if (!entityIn.m_6084_() && this.lightningRenderMap.containsKey(entityIn.m_20148_())) {
            this.lightningRenderMap.remove(entityIn.m_20148_());
        }
        poseStack.popPose();
    }

    private void rotateHandFor(PoseStack poseStack, MagnetronPartEntity part, MagnetronEntity.AttackPose attackPose, float poseProgress) {
        if (attackPose.isRotatedJoint(part.getJoint(), part.left)) {
            poseStack.mulPose(Axis.XN.rotationDegrees(90.0F * poseProgress));
        }
    }

    private void spawnLightningBetweenVecs(MagnetronEntity entityIn, Vec3 from, Vec3 end, LightningRender lightningRender, Vector4f color, float partialTicks) {
        float size = 0.1F;
        LightningBoltData.BoltRenderInfo blueBoltData = new LightningBoltData.BoltRenderInfo(0.05F, 0.05F, 0.0F, 0.0F, color, 0.3F);
        LightningBoltData bolt1 = new LightningBoltData(blueBoltData, from, end, 4).size(size).lifespan(2).spawn(LightningBoltData.SpawnFunction.NO_DELAY).fade(LightningBoltData.FadeFunction.fade(0.75F));
        if (!Minecraft.getInstance().isPaused()) {
            lightningRender.update(entityIn, bolt1, partialTicks);
        }
    }

    private void spawnLightningBetweenEntities(Entity start, Entity end, float centerUp, LightningRender lightningRender, Vector4f color, float partialTicks, Vec3 offset) {
        int segCount = 14;
        float spreadFactor = 0.05F;
        float size = 0.15F;
        Vec3 sizeAdjust = new Vec3((double) size, (double) size, (double) size);
        LightningBoltData.BoltRenderInfo blueBoltData = new LightningBoltData.BoltRenderInfo(0.05F, spreadFactor, 0.0F, 0.0F, color, 0.0F);
        LightningBoltData bolt1 = new LightningBoltData(blueBoltData, start.getPosition(partialTicks).add(sizeAdjust).add(offset.x, offset.y + (double) centerUp, offset.z), end.getPosition(partialTicks).add(0.0, 0.5, 0.0), segCount).size(size).lifespan(2).spawn(LightningBoltData.SpawnFunction.NO_DELAY).fade(LightningBoltData.FadeFunction.fade(0.75F));
        if (!Minecraft.getInstance().isPaused()) {
            lightningRender.update(end, bolt1, partialTicks);
        }
    }

    private LightningRender getLightingRender(UUID uuid) {
        if (this.lightningRenderMap.get(uuid) == null) {
            this.lightningRenderMap.put(uuid, new LightningRender());
        }
        return (LightningRender) this.lightningRenderMap.get(uuid);
    }

    public ResourceLocation getTextureLocation(MagnetronEntity entity) {
        return TEXTURE;
    }

    class LayerGlow extends RenderLayer<MagnetronEntity, MagnetronModel> {

        public LayerGlow() {
            super(MagnetronRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, MagnetronEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float alpha1 = (float) Math.sin((double) (ageInTicks * 0.1F)) * 0.5F + 0.5F;
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(MagnetronRenderer.TEXTURE_GLOW_BLUE));
            ((MagnetronModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha1);
            float alpha2 = (float) Math.cos((double) (ageInTicks * 0.1F)) * 0.5F + 0.5F;
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(MagnetronRenderer.TEXTURE_GLOW_RED));
            ((MagnetronModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha2);
            VertexConsumer ivertexbuilder3 = bufferIn.getBuffer(RenderType.eyes(MagnetronRenderer.TEXTURE_GLOW_EYES));
            ((MagnetronModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder3, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}