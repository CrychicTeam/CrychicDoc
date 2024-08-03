package com.mna.interop;

import com.mna.items.artifice.ItemFluidJug;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CurioRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        ICurioRenderer.translateIfSneaking(matrixStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(matrixStack, slotContext.entity());
        if (stack.getItem() instanceof ItemFluidJug) {
            matrixStack.translate(0.0F, 0.3F, 0.2F);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
        } else {
            matrixStack.translate(0.0F, 0.3F, 0.2F);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.scale(2.0F, 2.0F, 2.0F);
        }
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, mc.level, light);
        if (stack.getItem() instanceof ItemFluidJug) {
            FluidStack flStack = ((ItemFluidJug) stack.getItem()).getFluidTagData(stack);
            IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(flStack.getFluid());
            if (!flStack.isEmpty()) {
                float pct = MathUtils.clamp01((float) flStack.getAmount() / 16000.0F);
                matrixStack.translate(0.0F, -0.315F, 0.2F);
                this.renderFluidBar(matrixStack, renderTypeBuffer, light, partialTicks, extension, pct, ((ItemFluidJug) stack.getItem()).isInfinite());
            }
        }
        matrixStack.popPose();
    }

    private void renderFluidBar(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLight, float partialTicks, IClientFluidTypeExtensions attrs, float fillPct, boolean infinite) {
        float width = infinite ? 0.08F : 0.12F;
        float min = infinite ? 0.02F : 0.0F;
        float height = infinite ? 0.2F : 0.5F;
        ResourceLocation fluidTexBase = attrs.getStillTexture();
        if (fluidTexBase != null) {
            TextureAtlasSprite sp = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTexBase);
            RenderType liquid = RenderType.armorCutoutNoCull(TextureAtlas.LOCATION_BLOCKS);
            VertexConsumer builder = bufferIn.getBuffer(liquid);
            int color = attrs.getTintColor();
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color >> 0 & 0xFF) / 255.0F;
            float a = (float) (color >> 24 & 0xFF) / 255.0F;
            float[] rgba = new float[] { r, g, b, a };
            Matrix3f normal = ((PoseStack.Pose) matrixStackIn.poseStack.getLast()).normal();
            Matrix4f pos = ((PoseStack.Pose) matrixStackIn.poseStack.getLast()).pose();
            Vector3f nrm = new Vector3f(0.0F, 1.0F, 0.0F);
            nrm.mul(normal);
            float maxV = sp.getV0() + (sp.getV1() - sp.getV0()) * fillPct;
            float minU = sp.getU(0.0);
            float maxU = sp.getU(8.0);
            int light = 15728880;
            builder.vertex(pos, -width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(minU, sp.getV0());
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(maxU, sp.getV0());
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, width, min + height * fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(maxU, maxV);
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(pos, -width, min + height * fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(minU, maxV);
            builder.overlayCoords(OverlayTexture.NO_OVERLAY);
            builder.uv2(light);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
        }
    }
}