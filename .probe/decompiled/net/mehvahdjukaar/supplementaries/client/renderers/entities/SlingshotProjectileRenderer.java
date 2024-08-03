package net.mehvahdjukaar.supplementaries.client.renderers.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.supplementaries.common.entities.SlingshotProjectileEntity;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;

public class SlingshotProjectileRenderer<T extends SlingshotProjectileEntity & ItemSupplier> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public SlingshotProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(T entity, BlockPos pos) {
        return entity.getLightEmission();
    }

    public void render(T entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.translate(0.0F, -entity.m_20206_() / 2.0F, 0.0F);
        if (entity.f_19797_ >= 3 || this.f_114476_.camera.getEntity().distanceToSqr(entity) >= 12.25) {
            poseStack.pushPose();
            poseStack.translate(0.0, 0.25, 0.0);
            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - Mth.rotLerp(partialTicks, entity.f_19859_, entity.m_146908_())));
            poseStack.mulPose(Axis.ZN.rotationDegrees(Mth.rotLerp(partialTicks, entity.f_19860_, entity.m_146909_())));
            float scale = (float) ((Double) ClientConfigs.Items.SLINGSHOT_PROJECTILE_SCALE.get()).doubleValue();
            poseStack.scale(scale, scale, scale);
            this.itemRenderer.renderStatic(entity.m_7846_(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.m_9236_(), 0);
            poseStack.popPose();
            super.render(entity, pEntityYaw, partialTicks, poseStack, buffer, light);
        }
    }

    public ResourceLocation getTextureLocation(SlingshotProjectileEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}