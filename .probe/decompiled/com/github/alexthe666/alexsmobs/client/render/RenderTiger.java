package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelTiger;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerTigerEyes;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.joml.Matrix4f;

public class RenderTiger extends MobRenderer<EntityTiger, ModelTiger> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger.png");

    private static final ResourceLocation TEXTURE_ANGRY = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger_angry.png");

    private static final ResourceLocation TEXTURE_SLEEPING = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger_sleeping.png");

    private static final ResourceLocation TEXTURE_WHITE = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger_white.png");

    private static final ResourceLocation TEXTURE_ANGRY_WHITE = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger_white_angry.png");

    private static final ResourceLocation TEXTURE_SLEEPING_WHITE = new ResourceLocation("alexsmobs:textures/entity/tiger/tiger_white_sleeping.png");

    public RenderTiger(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelTiger(), 0.6F);
        this.m_115326_(new LayerTigerEyes(this));
    }

    protected void scale(EntityTiger entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public void render(EntityTiger entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) {
            matrixStackIn.pushPose();
            ((ModelTiger) this.f_115290_).f_102608_ = this.m_115342_(entityIn, partialTicks);
            boolean shouldSit = entityIn.m_20159_() && entityIn.m_20202_() != null && entityIn.m_20202_().shouldRiderSit();
            ((ModelTiger) this.f_115290_).f_102609_ = shouldSit;
            ((ModelTiger) this.f_115290_).f_102610_ = entityIn.m_6162_();
            float f = Mth.rotLerp(partialTicks, entityIn.f_20884_, entityIn.f_20883_);
            float f1 = Mth.rotLerp(partialTicks, entityIn.f_20886_, entityIn.f_20885_);
            float f2 = f1 - f;
            if (shouldSit && entityIn.m_20202_() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn.m_20202_();
                f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
                f2 = f1 - f;
                float f3 = Mth.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }
                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }
                f2 = f1 - f;
            }
            float f6 = Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_());
            if (entityIn.m_20089_() == Pose.SLEEPING) {
                Direction direction = entityIn.m_21259_();
                if (direction != null) {
                    float f4 = entityIn.m_20236_(Pose.STANDING) - 0.1F;
                    matrixStackIn.translate((double) ((float) (-direction.getStepX()) * f4), 0.0, (double) ((float) (-direction.getStepZ()) * f4));
                }
            }
            float f7 = this.m_6930_(entityIn, partialTicks);
            this.m_7523_(entityIn, matrixStackIn, f7, f, partialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            this.scale(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0.0, -1.501F, 0.0);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && entityIn.m_6084_()) {
                f8 = entityIn.f_267362_.speed(partialTicks);
                f5 = entityIn.f_267362_.position(partialTicks);
                if (entityIn.m_6162_()) {
                    f5 *= 3.0F;
                }
                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            ((ModelTiger) this.f_115290_).m_6839_(entityIn, f5, f8, partialTicks);
            ((ModelTiger) this.f_115290_).setupAnim(entityIn, f5, f8, f7, f2, f6);
            Minecraft minecraft = Minecraft.getInstance();
            boolean flag = this.m_5933_(entityIn);
            boolean flag1 = !flag && !entityIn.m_20177_(minecraft.player);
            boolean flag2 = minecraft.shouldEntityAppearGlowing(entityIn);
            RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
            if (rendertype != null) {
                float stealthLevel = entityIn.prevStealthProgress + (entityIn.stealthProgress - entityIn.prevStealthProgress) * partialTicks;
                this.f_114477_ = 0.6F * (1.0F - stealthLevel * 0.1F);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
                int i = m_115338_(entityIn, this.m_6931_(entityIn, partialTicks));
                ((ModelTiger) this.f_115290_).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : Mth.clamp(1.0F - stealthLevel * 0.1F, 0.0F, 1.0F));
            }
            if (!entityIn.m_5833_()) {
                for (RenderLayer layerrenderer : this.f_115291_) {
                    layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
                }
            }
            matrixStackIn.popPose();
            Entity entity = entityIn.m_21524_();
            if (entity != null) {
                this.renderLeash(entityIn, partialTicks, matrixStackIn, bufferIn, entity);
            }
            RenderNameTagEvent renderNameplateEvent = new RenderNameTagEvent(entityIn, entityIn.m_5446_(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
            if (renderNameplateEvent.getResult() != Result.DENY && (renderNameplateEvent.getResult() == Result.ALLOW || this.m_6512_(entityIn))) {
                this.m_7649_(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
            }
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
        }
    }

    private <E extends Entity> void renderLeash(EntityTiger tiger, float float0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, E e3) {
        poseStack1.pushPose();
        Vec3 vec3 = e3.getRopeHoldPosition(float0);
        double d0 = (double) (Mth.lerp(float0, tiger.f_20883_, tiger.f_20884_) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
        Vec3 vec31 = tiger.m_245894_(float0);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double) float0, tiger.f_19854_, tiger.m_20185_()) + d1;
        double d4 = Mth.lerp((double) float0, tiger.f_19855_, tiger.m_20186_()) + vec31.y;
        double d5 = Mth.lerp((double) float0, tiger.f_19856_, tiger.m_20189_()) + d2;
        poseStack1.translate(d1, vec31.y, d2);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = multiBufferSource2.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack1.last().pose();
        float f4 = (float) (Mth.fastInvSqrt((double) (f * f + f2 * f2)) * 0.025F / 2.0);
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = AMBlockPos.fromVec3(tiger.m_20299_(float0));
        BlockPos blockpos1 = AMBlockPos.fromVec3(e3.getEyePosition(float0));
        int i = this.getBlockLightLevel(tiger, blockpos);
        int j = this.getBlockLightLevel(tiger, blockpos1);
        int k = tiger.m_9236_().m_45517_(LightLayer.SKY, blockpos);
        int l = tiger.m_9236_().m_45517_(LightLayer.SKY, blockpos1);
        for (int i1 = 0; i1 <= 24; i1++) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; j1--) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }
        poseStack1.popPose();
    }

    protected int getBlockLightLevel(EntityTiger entityTiger0, BlockPos blockPos1) {
        return entityTiger0.m_6060_() ? 15 : entityTiger0.m_9236_().m_45517_(LightLayer.BLOCK, blockPos1);
    }

    private static void addVertexPair(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3, float float4, int int5, int int6, int int7, int int8, float float9, float float10, float float11, float float12, int int13, boolean boolean14) {
        float f = (float) int13 / 24.0F;
        int i = (int) Mth.lerp(f, (float) int5, (float) int6);
        int j = (int) Mth.lerp(f, (float) int7, (float) int8);
        int k = LightTexture.pack(i, j);
        float f1 = int13 % 2 == (boolean14 ? 1 : 0) ? 0.7F : 1.0F;
        float f2 = 0.5F * f1;
        float f3 = 0.4F * f1;
        float f4 = 0.3F * f1;
        float f5 = float2 * f;
        float f6 = float3 > 0.0F ? float3 * f * f : float3 - float3 * (1.0F - f) * (1.0F - f);
        float f7 = float4 * f;
        vertexConsumer0.vertex(matrixF1, f5 - float11, f6 + float10, f7 + float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        vertexConsumer0.vertex(matrixF1, f5 + float11, f6 + float9 - float10, f7 - float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    protected int getBlockLight2(Entity entityIn, BlockPos partialTicks) {
        return entityIn.isOnFire() ? 15 : entityIn.level().m_45517_(LightLayer.BLOCK, partialTicks);
    }

    @Nullable
    protected RenderType getRenderType(EntityTiger tiger, boolean b0, boolean b1, boolean b2) {
        if (tiger.isStealth()) {
            ResourceLocation resourcelocation = this.getTextureLocation(tiger);
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else {
            return super.m_7225_(tiger, b0, b1, b2);
        }
    }

    public ResourceLocation getTextureLocation(EntityTiger entity) {
        if (entity.isSleeping()) {
            return entity.isWhite() ? TEXTURE_SLEEPING_WHITE : TEXTURE_SLEEPING;
        } else if (entity.getRemainingPersistentAngerTime() > 0) {
            return entity.isWhite() ? TEXTURE_ANGRY_WHITE : TEXTURE_ANGRY;
        } else {
            return entity.isWhite() ? TEXTURE_WHITE : TEXTURE;
        }
    }
}