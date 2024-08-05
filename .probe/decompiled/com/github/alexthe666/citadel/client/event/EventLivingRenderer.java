package com.github.alexthe666.citadel.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class EventLivingRenderer extends Event {

    private LivingEntity entity;

    private EntityModel model;

    private PoseStack poseStack;

    private float partialTicks;

    public EventLivingRenderer(LivingEntity entity, EntityModel model, PoseStack poseStack, float partialTicks) {
        this.entity = entity;
        this.model = model;
        this.poseStack = poseStack;
        this.partialTicks = partialTicks;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public EntityModel getModel() {
        return this.model;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static class AccessToBufferSource extends EventLivingRenderer {

        private float bodyYRot;

        private MultiBufferSource bufferSource;

        private int packedLight;

        public AccessToBufferSource(LivingEntity entity, EntityModel model, PoseStack poseStack, float bodyYRot, float partialTicks, MultiBufferSource bufferSource, int packedLight) {
            super(entity, model, poseStack, partialTicks);
            this.bodyYRot = bodyYRot;
            this.bufferSource = bufferSource;
            this.packedLight = packedLight;
        }

        public float getBodyYRot() {
            return this.bodyYRot;
        }

        public MultiBufferSource getBufferSource() {
            return this.bufferSource;
        }

        public int getPackedLight() {
            return this.packedLight;
        }
    }

    public static class PostRenderModel extends EventLivingRenderer.AccessToBufferSource {

        public PostRenderModel(LivingEntity entity, EntityModel model, PoseStack poseStack, float bodyYRot, float partialTicks, MultiBufferSource bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }

    public static class PostSetupAnimations extends EventLivingRenderer.AccessToBufferSource {

        public PostSetupAnimations(LivingEntity entity, EntityModel model, PoseStack poseStack, float bodyYRot, float partialTicks, MultiBufferSource bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }

    public static class PreSetupAnimations extends EventLivingRenderer.AccessToBufferSource {

        public PreSetupAnimations(LivingEntity entity, EntityModel model, PoseStack poseStack, float bodyYRot, float partialTicks, MultiBufferSource bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }

    public static class SetupRotations extends EventLivingRenderer {

        private float bodyYRot;

        public SetupRotations(LivingEntity entity, EntityModel model, PoseStack poseStack, float bodyYRot, float partialTicks) {
            super(entity, model, poseStack, partialTicks);
            this.bodyYRot = bodyYRot;
        }

        public float getBodyYRot() {
            return this.bodyYRot;
        }
    }
}