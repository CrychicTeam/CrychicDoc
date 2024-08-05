package me.fengming.renderjs.events.level;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

@Info("Invoked on rendering a living entity.\n")
public class RenderEntityEventJS extends RenderEventJS {

    public PoseStack poseStack;

    public MultiBufferSource multiBufferSource;

    public LivingEntity entity;

    public LivingEntityRenderer<?, ?> renderer;

    public float partialTick;

    public int packedLight;

    protected RenderEntityEventJS(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        this.entity = entity;
        this.renderer = renderer;
        this.partialTick = partialTick;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLight = packedLight;
    }

    @Override
    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return this.multiBufferSource;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public LivingEntityRenderer<?, ?> getRenderer() {
        return this.renderer;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public int getPackedLight() {
        return this.packedLight;
    }

    @Info("Invoked after rendering a entity.\n")
    public static class After extends RenderEntityEventJS {

        public After(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
            super(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight);
        }
    }

    @Info("Invoked before rendering a entity.\n")
    public static class Before extends RenderEntityEventJS {

        public Before(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
            super(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight);
        }
    }
}