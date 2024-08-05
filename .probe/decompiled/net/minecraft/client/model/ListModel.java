package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class ListModel<E extends Entity> extends EntityModel<E> {

    public ListModel() {
        this(RenderType::m_110458_);
    }

    public ListModel(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0) {
        super(functionResourceLocationRenderType0);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.parts().forEach(p_103030_ -> p_103030_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
    }

    public abstract Iterable<ModelPart> parts();
}