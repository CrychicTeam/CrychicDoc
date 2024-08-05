package noobanidus.mods.lootr.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.lootr.client.item.LootrChestItemRenderer;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

public class LootrChestCartRenderer<T extends LootrChestMinecartEntity> extends MinecartRenderer<T> {

    public LootrChestCartRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context, modelLayerLocation);
    }

    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.pushPose();
        long i = (long) pEntity.m_19879_() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        pMatrixStack.translate(f, f1, f2);
        double d0 = Mth.lerp((double) pPartialTicks, pEntity.f_19790_, pEntity.m_20185_());
        double d1 = Mth.lerp((double) pPartialTicks, pEntity.f_19791_, pEntity.m_20186_());
        double d2 = Mth.lerp((double) pPartialTicks, pEntity.f_19792_, pEntity.m_20189_());
        Vec3 vec3 = pEntity.m_38179_(d0, d1, d2);
        float f3 = Mth.lerp(pPartialTicks, pEntity.f_19860_, pEntity.m_146909_());
        if (vec3 != null) {
            Vec3 vec31 = pEntity.m_38096_(d0, d1, d2, 0.3F);
            Vec3 vec32 = pEntity.m_38096_(d0, d1, d2, -0.3F);
            if (vec31 == null) {
                vec31 = vec3;
            }
            if (vec32 == null) {
                vec32 = vec3;
            }
            pMatrixStack.translate(vec3.x - d0, (vec31.y + vec32.y) / 2.0 - d1, vec3.z - d2);
            Vec3 vec33 = vec32.add(-vec31.x, -vec31.y, -vec31.z);
            if (vec33.length() != 0.0) {
                vec33 = vec33.normalize();
                pEntityYaw = (float) (Math.atan2(vec33.z, vec33.x) * 180.0 / Math.PI);
                f3 = (float) (Math.atan(vec33.y) * 73.0);
            }
        }
        pMatrixStack.translate(0.0, 0.375, 0.0);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        pMatrixStack.mulPose(Axis.ZP.rotationDegrees(-f3));
        float f5 = (float) pEntity.m_38176_() - pPartialTicks;
        float f6 = pEntity.m_38169_() - pPartialTicks;
        if (f6 < 0.0F) {
            f6 = 0.0F;
        }
        if (f5 > 0.0F) {
            pMatrixStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f5) * f5 * f6 / 10.0F * (float) pEntity.m_38177_()));
        }
        int j = pEntity.m_38183_();
        pMatrixStack.pushPose();
        pMatrixStack.scale(0.75F, 0.75F, 0.75F);
        pMatrixStack.translate(-0.5, (double) ((float) (j - 8) / 16.0F), 0.5);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        LootrChestItemRenderer.getInstance().renderByMinecart(pEntity, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.f_115401_.setupAnim(pEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.f_115401_.m_103119_(this.m_5478_(pEntity)));
        this.f_115401_.m_7695_(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
    }
}