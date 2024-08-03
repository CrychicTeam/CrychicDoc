package net.liopyu.entityjs.client.nonliving;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.liopyu.entityjs.builders.nonliving.entityjs.ProjectileEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IProjectileEntityJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class KubeJSProjectileEntityRenderer<T extends Entity & IProjectileEntityJS> extends EntityRenderer<T> {

    private final ProjectileEntityBuilder<T> builder;

    public static RenderType RENDER_TYPE;

    public KubeJSProjectileEntityRenderer(EntityRendererProvider.Context renderManager, ProjectileEntityBuilder<T> builder) {
        super(renderManager);
        this.builder = builder;
        RENDER_TYPE = RenderType.entityCutoutNoCull(this.getDynamicTextureLocation());
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        if (this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pX != null && this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pY != null && this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pZ != null) {
            float pX = this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pX;
            float pY = this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pY;
            float pZ = this.builder.renderScale(this.builder.pX, this.builder.pY, this.builder.pZ).pZ;
            pMatrixStack.scale(pX, pY, pZ);
        } else {
            pMatrixStack.scale(2.0F, 2.0F, 2.0F);
        }
        pMatrixStack.mulPose(this.f_114476_.cameraOrientation());
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose $$6 = pMatrixStack.last();
        Matrix4f $$7 = $$6.pose();
        Matrix3f $$8 = $$6.normal();
        VertexConsumer $$9 = pBuffer.getBuffer(RENDER_TYPE);
        this.vertex($$9, $$7, $$8, pPackedLight, 0.0F, 0, 0, 1);
        this.vertex($$9, $$7, $$8, pPackedLight, 1.0F, 0, 1, 1);
        this.vertex($$9, $$7, $$8, pPackedLight, 1.0F, 1, 1, 0);
        this.vertex($$9, $$7, $$8, pPackedLight, 0.0F, 1, 0, 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pMatrixStack, pBuffer, pPackedLight);
    }

    public void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, int int5, int int6, int int7) {
        if (this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vX != null && this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vY != null && this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vZ != null) {
            float vX = this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vX;
            float vY = this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vY;
            float vZ = this.builder.renderOffset(this.builder.vX, this.builder.vY, this.builder.vZ).vZ;
            vertexConsumer0.vertex(matrixF1, float4 + vX, (float) int5 + vY, (float) int6 + vZ).color(255, 255, 255, 255).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
        } else {
            vertexConsumer0.vertex(matrixF1, float4, (float) int5, -0.5F).color(255, 255, 255, 255).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return (ResourceLocation) this.builder.textureLocation.apply(entity);
    }

    private ResourceLocation getDynamicTextureLocation() {
        return new ResourceLocation(this.builder.id.getNamespace() + ":textures/entity/projectiles/" + this.builder.id.getPath() + ".png");
    }
}