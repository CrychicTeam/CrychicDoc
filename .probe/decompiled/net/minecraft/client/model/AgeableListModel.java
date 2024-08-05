package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class AgeableListModel<E extends Entity> extends EntityModel<E> {

    private final boolean scaleHead;

    private final float babyYHeadOffset;

    private final float babyZHeadOffset;

    private final float babyHeadScale;

    private final float babyBodyScale;

    private final float bodyYOffset;

    protected AgeableListModel(boolean boolean0, float float1, float float2) {
        this(boolean0, float1, float2, 2.0F, 2.0F, 24.0F);
    }

    protected AgeableListModel(boolean boolean0, float float1, float float2, float float3, float float4, float float5) {
        this(RenderType::m_110458_, boolean0, float1, float2, float3, float4, float5);
    }

    protected AgeableListModel(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0, boolean boolean1, float float2, float float3, float float4, float float5, float float6) {
        super(functionResourceLocationRenderType0);
        this.scaleHead = boolean1;
        this.babyYHeadOffset = float2;
        this.babyZHeadOffset = float3;
        this.babyHeadScale = float4;
        this.babyBodyScale = float5;
        this.bodyYOffset = float6;
    }

    protected AgeableListModel() {
        this(false, 5.0F, 2.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        if (this.f_102610_) {
            poseStack0.pushPose();
            if (this.scaleHead) {
                float $$8 = 1.5F / this.babyHeadScale;
                poseStack0.scale($$8, $$8, $$8);
            }
            poseStack0.translate(0.0F, this.babyYHeadOffset / 16.0F, this.babyZHeadOffset / 16.0F);
            this.headParts().forEach(p_102081_ -> p_102081_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
            poseStack0.pushPose();
            float $$9 = 1.0F / this.babyBodyScale;
            poseStack0.scale($$9, $$9, $$9);
            poseStack0.translate(0.0F, this.bodyYOffset / 16.0F, 0.0F);
            this.bodyParts().forEach(p_102071_ -> p_102071_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
        } else {
            this.headParts().forEach(p_102061_ -> p_102061_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            this.bodyParts().forEach(p_102051_ -> p_102051_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
        }
    }

    protected abstract Iterable<ModelPart> headParts();

    protected abstract Iterable<ModelPart> bodyParts();
}