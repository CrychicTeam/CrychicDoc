package noppes.npcs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.entity.EntityProjectile;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderProjectile<T extends EntityProjectile> extends EntityRenderer<T> {

    public boolean renderWithColor = true;

    private static final ResourceLocation field_110780_a = new ResourceLocation("textures/entity/projectiles/arrow.png");

    private static final ResourceLocation field_110798_h = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private boolean crash = false;

    private boolean crash2 = false;

    public RenderProjectile(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public void render(T projectile, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        matrixStack.pushPose();
        float scale = (float) projectile.getSize() / 10.0F;
        ItemStack item = projectile.getItemDisplay();
        matrixStack.scale(scale, scale, scale);
        if (projectile.isArrow()) {
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, projectile.f_19859_, projectile.m_146908_()) - 90.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, projectile.f_19860_, projectile.m_146909_())));
            float f9 = (float) projectile.arrowShake - partialTicks;
            if (f9 > 0.0F) {
                float f10 = -Mth.sin(f9 * 3.0F) * f9;
                matrixStack.mulPose(Axis.ZP.rotationDegrees(f10));
            }
            matrixStack.mulPose(Axis.XP.rotationDegrees(45.0F));
            matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
            matrixStack.translate(-4.0, 0.0, 0.0);
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(projectile)));
            PoseStack.Pose matrixstack$entry = matrixStack.last();
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);
            for (int j = 0; j < 4; j++) {
                matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
                this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
                this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
                this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
            }
        } else if (projectile.is3D()) {
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, projectile.f_19859_, projectile.m_146908_()) - 180.0F));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, projectile.f_19860_, projectile.m_146909_())));
            matrixStack.translate(0.0F, -0.125F, 0.25F);
            if (item.getItem() instanceof BlockItem && Block.byItem(item.getItem()).defaultBlockState().m_60799_() == RenderShape.ENTITYBLOCK_ANIMATED) {
                matrixStack.translate(0.0F, 0.1875F, -0.3125F);
                matrixStack.mulPose(Axis.XP.rotationDegrees(20.0F));
                matrixStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                float f8 = 0.375F;
                matrixStack.scale(-f8, -f8, f8);
            }
            if (!this.crash) {
                try {
                    mc.getItemRenderer().renderStatic(item, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
                } catch (Throwable var17) {
                    this.crash = true;
                }
            } else if (!this.crash2) {
                try {
                    mc.getItemRenderer().renderStatic(item, ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
                } catch (Throwable var16) {
                    this.crash2 = true;
                }
            } else {
                mc.getItemRenderer().renderStatic(new ItemStack(Blocks.DIRT), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
            }
        } else {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(this.f_114476_.camera.rotation());
            matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            mc.getItemRenderer().renderStatic(item, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
        }
        if (projectile.is3D() && projectile.glows()) {
        }
        matrixStack.popPose();
    }

    protected ResourceLocation func_110779_a(EntityProjectile projectile) {
        return projectile.isArrow() ? field_110780_a : TextureAtlas.LOCATION_BLOCKS;
    }

    public ResourceLocation getTextureLocation(T par1Entity) {
        return par1Entity.isArrow() ? field_110780_a : TextureAtlas.LOCATION_BLOCKS;
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int p_229039_9_, int p_229039_10_, int p_229039_11_, int packedLightIn) {
        vertexBuilder.vertex(matrix, (float) offsetX, (float) offsetY, (float) offsetZ).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, (float) p_229039_9_, (float) p_229039_11_, (float) p_229039_10_).endVertex();
    }
}