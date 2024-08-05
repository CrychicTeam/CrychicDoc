package com.github.alexthe666.citadel.client.model.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class BasicEntityModel<T extends Entity> extends EntityModel<T> {

    public int textureWidth = 64;

    public int textureHeight = 32;

    protected BasicEntityModel() {
        this(RenderType::m_110458_);
    }

    protected BasicEntityModel(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0) {
        super(functionResourceLocationRenderType0);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.parts().forEach(p_103030_ -> p_103030_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
    }

    public abstract Iterable<BasicModelPart> parts();

    @Override
    public abstract void setupAnim(T var1, float var2, float var3, float var4, float var5, float var6);

    @Override
    public void prepareMobModel(T t0, float float1, float float2, float float3) {
    }
}