package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

public abstract class ColorableAgeableListModel<E extends Entity> extends AgeableListModel<E> {

    private float r = 1.0F;

    private float g = 1.0F;

    private float b = 1.0F;

    public void setColor(float float0, float float1, float float2) {
        this.r = float0;
        this.g = float1;
        this.b = float2;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        super.renderToBuffer(poseStack0, vertexConsumer1, int2, int3, this.r * float4, this.g * float5, this.b * float6, float7);
    }
}