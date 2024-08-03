package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.WatcherModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;

public class WatcherRenderer extends MobRenderer<WatcherEntity, WatcherModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/watcher.png");

    private static final ResourceLocation TEXTURE_MOTH = new ResourceLocation("alexscaves:textures/entity/watcher_moth.png");

    private static final ResourceLocation TEXTURE_EYESPOTS = new ResourceLocation("alexscaves:textures/entity/watcher_eyespots.png");

    public WatcherRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new WatcherModel(), 0.5F);
        this.m_115326_(new WatcherRenderer.LayerGlow());
    }

    protected void scale(WatcherEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public void render(WatcherEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entity, this, partialTicks, poseStack, bufferSource, light))) {
            poseStack.pushPose();
            ((WatcherModel) this.f_115290_).f_102608_ = this.m_115342_(entity, partialTicks);
            boolean shouldSit = entity.m_20159_() && entity.m_20202_() != null && entity.m_20202_().shouldRiderSit();
            ((WatcherModel) this.f_115290_).f_102609_ = shouldSit;
            ((WatcherModel) this.f_115290_).f_102610_ = entity.m_6162_();
            float f = Mth.rotLerp(partialTicks, entity.f_20884_, entity.f_20883_);
            float f1 = Mth.rotLerp(partialTicks, entity.f_20886_, entity.f_20885_);
            float f2 = f1 - f;
            if (shouldSit && entity.m_20202_() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity.m_20202_();
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
            float f6 = Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_());
            if (m_194453_(entity)) {
                f6 *= -1.0F;
                f2 *= -1.0F;
            }
            if (entity.m_217003_(Pose.SLEEPING)) {
                Direction direction = entity.m_21259_();
                if (direction != null) {
                    float f4 = entity.m_20236_(Pose.STANDING) - 0.1F;
                    poseStack.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
                }
            }
            float f7 = this.m_6930_(entity, partialTicks);
            this.m_7523_(entity, poseStack, f7, f, partialTicks);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(entity, poseStack, partialTicks);
            poseStack.translate(0.0F, -1.501F, 0.0F);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && entity.m_6084_()) {
                f8 = entity.f_267362_.speed(partialTicks);
                f5 = entity.f_267362_.position(partialTicks);
                if (entity.m_6162_()) {
                    f5 *= 3.0F;
                }
                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            ((WatcherModel) this.f_115290_).m_6839_(entity, f5, f8, partialTicks);
            ((WatcherModel) this.f_115290_).setupAnim(entity, f5, f8, f7, f2, f6);
            Minecraft minecraft = Minecraft.getInstance();
            boolean flag = this.m_5933_(entity);
            boolean flag1 = !flag && !entity.m_20177_(minecraft.player);
            boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
            RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
            if (rendertype != null) {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(rendertype);
                int i = m_115338_(entity, this.m_6931_(entity, partialTicks));
                float transparency = this.getWatcherTransparency(entity, partialTicks);
                ((WatcherModel) this.f_115290_).m_7695_(poseStack, vertexconsumer, light, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F * transparency : transparency);
            }
            if (!entity.m_5833_()) {
                for (RenderLayer<WatcherEntity, WatcherModel> renderlayer : this.f_115291_) {
                    renderlayer.render(poseStack, bufferSource, light, entity, f5, f8, partialTicks, f7, f2, f6);
                }
            }
            RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity, entity.m_5446_(), this, poseStack, bufferSource, light, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
            if (renderNameTagEvent.getResult() != Result.DENY && (renderNameTagEvent.getResult() == Result.ALLOW || this.m_6512_(entity))) {
                this.m_7649_(entity, renderNameTagEvent.getContent(), poseStack, bufferSource, light);
            }
            poseStack.popPose();
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entity, this, partialTicks, poseStack, bufferSource, light));
        }
    }

    private float getWatcherTransparency(WatcherEntity entity, float partialTicks) {
        return (1.0F - entity.getShadeAmount(partialTicks)) * 0.8F + 0.2F;
    }

    @Nullable
    protected RenderType getRenderType(WatcherEntity entity, boolean visible, boolean invisible, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(entity);
        return RenderType.entityTranslucent(resourcelocation);
    }

    public ResourceLocation getTextureLocation(WatcherEntity entity) {
        return TEXTURE;
    }

    class LayerGlow extends RenderLayer<WatcherEntity, WatcherModel> {

        public LayerGlow() {
            super(WatcherRenderer.this);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, WatcherEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.entityCutoutNoCull(WatcherRenderer.TEXTURE_MOTH));
            ((WatcherModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(WatcherRenderer.TEXTURE_EYESPOTS));
            ((WatcherModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 0.66F);
        }
    }
}