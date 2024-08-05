package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import org.joml.Matrix4f;

public class TheEndPortalRenderer<T extends TheEndPortalBlockEntity> implements BlockEntityRenderer<T> {

    public static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");

    public static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation("textures/entity/end_portal.png");

    public TheEndPortalRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
    }

    public void render(T t0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Matrix4f $$6 = poseStack2.last().pose();
        this.renderCube(t0, $$6, multiBufferSource3.getBuffer(this.renderType()));
    }

    private void renderCube(T t0, Matrix4f matrixF1, VertexConsumer vertexConsumer2) {
        float $$3 = this.getOffsetDown();
        float $$4 = this.getOffsetUp();
        this.renderFace(t0, matrixF1, vertexConsumer2, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(t0, matrixF1, vertexConsumer2, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(t0, matrixF1, vertexConsumer2, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(t0, matrixF1, vertexConsumer2, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(t0, matrixF1, vertexConsumer2, 0.0F, 1.0F, $$3, $$3, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(t0, matrixF1, vertexConsumer2, 0.0F, 1.0F, $$4, $$4, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(T t0, Matrix4f matrixF1, VertexConsumer vertexConsumer2, float float3, float float4, float float5, float float6, float float7, float float8, float float9, float float10, Direction direction11) {
        if (t0.shouldRenderFace(direction11)) {
            vertexConsumer2.vertex(matrixF1, float3, float5, float7).endVertex();
            vertexConsumer2.vertex(matrixF1, float4, float5, float8).endVertex();
            vertexConsumer2.vertex(matrixF1, float4, float6, float9).endVertex();
            vertexConsumer2.vertex(matrixF1, float3, float6, float10).endVertex();
        }
    }

    protected float getOffsetUp() {
        return 0.75F;
    }

    protected float getOffsetDown() {
        return 0.375F;
    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }
}