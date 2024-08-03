package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public abstract class HierarchicalModel<E extends Entity> extends EntityModel<E> {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public HierarchicalModel() {
        this(RenderType::m_110458_);
    }

    public HierarchicalModel(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0) {
        super(functionResourceLocationRenderType0);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.root().render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }

    public abstract ModelPart root();

    public Optional<ModelPart> getAnyDescendantWithName(String string0) {
        return string0.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter(p_233400_ -> p_233400_.hasChild(string0)).findFirst().map(p_233397_ -> p_233397_.getChild(string0));
    }

    protected void animate(AnimationState animationState0, AnimationDefinition animationDefinition1, float float2) {
        this.animate(animationState0, animationDefinition1, float2, 1.0F);
    }

    protected void animateWalk(AnimationDefinition animationDefinition0, float float1, float float2, float float3, float float4) {
        long $$5 = (long) (float1 * 50.0F * float3);
        float $$6 = Math.min(float2 * float4, 1.0F);
        KeyframeAnimations.animate(this, animationDefinition0, $$5, $$6, ANIMATION_VECTOR_CACHE);
    }

    protected void animate(AnimationState animationState0, AnimationDefinition animationDefinition1, float float2, float float3) {
        animationState0.updateTime(float2, float3);
        animationState0.ifStarted(p_233392_ -> KeyframeAnimations.animate(this, animationDefinition1, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
    }

    protected void applyStatic(AnimationDefinition animationDefinition0) {
        KeyframeAnimations.animate(this, animationDefinition0, 0L, 1.0F, ANIMATION_VECTOR_CACHE);
    }
}