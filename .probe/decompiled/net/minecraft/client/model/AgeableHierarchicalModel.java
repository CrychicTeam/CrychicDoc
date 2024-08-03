package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class AgeableHierarchicalModel<E extends Entity> extends HierarchicalModel<E> {

    private final float youngScaleFactor;

    private final float bodyYOffset;

    public AgeableHierarchicalModel(float float0, float float1) {
        this(float0, float1, RenderType::m_110458_);
    }

    public AgeableHierarchicalModel(float float0, float float1, Function<ResourceLocation, RenderType> functionResourceLocationRenderType2) {
        super(functionResourceLocationRenderType2);
        this.bodyYOffset = float1;
        this.youngScaleFactor = float0;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        if (this.f_102610_) {
            poseStack0.pushPose();
            poseStack0.scale(this.youngScaleFactor, this.youngScaleFactor, this.youngScaleFactor);
            poseStack0.translate(0.0F, this.bodyYOffset / 16.0F, 0.0F);
            this.m_142109_().render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
            poseStack0.popPose();
        } else {
            this.m_142109_().render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
        }
    }
}